package com.example.ammar.teacherandmev10.IdentifierClasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.example.ammar.teacherandmev10.TeacherDrawer.CustomAdapterAQTE;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ammar on 2018-01-08.
 */

public class DatabaseAccessFunctions
{
    private static CustomAdapterAQTE customAdapterAQTE;
    public ArrayList<String> getChildrenOfDatabaseKeys(Iterator<DataSnapshot> iterator, ArrayList<String> toUpdate)
    {
        if(toUpdate.size() > 0)
            toUpdate.clear();

        while (iterator.hasNext())
        {
            toUpdate.add(String.valueOf(iterator.next().getKey()));
        }
        return toUpdate;
    }

    public ArrayList<String> getChildrenOfDatabaseValues(Iterator<DataSnapshot> iterator, ArrayList<String> toUpdate, String key)
    {
        if(toUpdate.size() > 0)
            toUpdate.clear();

        while (iterator.hasNext())
        {
            toUpdate.add(String.valueOf(iterator.next().child(key).getValue()));
        }
        return toUpdate;
    }

    public void addChild(final DatabaseReference db, final Object toAdd, String name, Context context) //called when enroll students is clicked
    {
        final Map<String,Object> childrenToAdd = new HashMap<>();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Add a " + name );
        alert.setMessage("Enter the name of the " + name + " you'd like to add");
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = input.getText().toString();
                childrenToAdd.put(name, toAdd);
                db.getRef().updateChildren(childrenToAdd);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public DatabaseReference getCourses()
    {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(getUserEntryToDB()).child("teacher").child("courses"); //email-teacher
    }

    public DatabaseReference getChildOfCourses(String courseName)
    {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(getUserEntryToDB()).child("teacher").child("courses").child(courseName);
    }

    private String getUserEntryToDB()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String refEmail = currentUser.getEmail();
        User temp = new User();
        return temp.setUniqueID(refEmail);
    }

    public DatabaseReference getClassList(String courseName)
    {
        return getChildOfCourses(courseName).child("classList");
    }

    public DatabaseReference getAssignments(String courseName)
    {
        return getChildOfCourses(courseName).child("assignments");
    }

    public DatabaseReference getQuizzes(String courseName)
    {
        return getChildOfCourses(courseName).child("quizzes");
    }

    public DatabaseReference getTests(String courseName)
    {
        return getChildOfCourses(courseName).child("tests");
    }

    public DatabaseReference getExams(String courseName)
    {
        return getChildOfCourses(courseName).child("exams");
    }

    public CustomAdapterAQTE AQTEDatesAndName(DatabaseReference db,final Activity act, final Context context )
    {
        db.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterator<DataSnapshot> iterator1 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = dataSnapshot.getChildren().iterator();

                ArrayList<String> assNames = getChildrenOfDatabaseKeys(iterator1, new ArrayList<String>());
                ArrayList<String> assignedDate = getChildrenOfDatabaseValues(iterator2, new ArrayList<String>(),"assignedDate");
                ArrayList<String> dueDate = getChildrenOfDatabaseValues(iterator3, new ArrayList<String>(),"dueDate");

                String [] assignments = assNames.toArray(new String[0]);
                String [] assDate = assignedDate.toArray(new String[0]);
                String [] dDate = dueDate.toArray(new String[0]);

                customAdapterAQTE = new CustomAdapterAQTE(act,assignments,context,assDate,dDate);
                //assignmentList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        return customAdapterAQTE;
    }

}
