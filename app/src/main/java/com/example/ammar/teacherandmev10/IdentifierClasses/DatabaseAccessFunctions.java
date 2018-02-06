package com.example.ammar.teacherandmev10.IdentifierClasses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private static boolean check = false;
    private static CustomAdapterAQTE customAdapterAQTE;

    public String [] getChildrenOfDatabaseKeys(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> toUpdate = new ArrayList<>();

        if(toUpdate.size() > 0)
            toUpdate.clear();

        while (iterator.hasNext())
        {
            toUpdate.add(String.valueOf(iterator.next().getKey()));
        }
        return toUpdate.toArray(new String[0]);
    }

    public String [] getChildrenOfDatabaseValues(Iterator<DataSnapshot> iterator, String key)
    {
        ArrayList<String> toUpdate = new ArrayList<>();

        if(toUpdate.size() > 0)
            toUpdate.clear();

        while (iterator.hasNext())
        {
            toUpdate.add(String.valueOf(iterator.next().child(key).getValue()));
        }
        return toUpdate.toArray(new String[0]);
    }

    public void addChild(final DatabaseReference db, final Object toAdd, String name, Context context)  //called when enroll students is clicked
    {
        final Map<String,Object> childrenToAdd = new HashMap<>();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Add a " + name );
        alert.setMessage("Enter the name of the " + name + " you'd like to add");
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String name = input.getText().toString();
                childrenToAdd.put(name, toAdd);
                db.getRef().updateChildren(childrenToAdd);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Cancelled.
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

    public void removeAQTEFromStudents(Iterator<DataSnapshot> iterator, String AQTE, String name, DatabaseReference classList)
    {
        while (iterator.hasNext())
        {
            classList.child(iterator.next().getKey()).child(AQTE).child(name).removeValue();
        }
    }

    public DatabaseReference getAQTEofStudent(String courseName, String studentName, String AQTE)
    {
       return getChildOfCourses(courseName).child("classList").child(studentName).child(AQTE);
    }
}
