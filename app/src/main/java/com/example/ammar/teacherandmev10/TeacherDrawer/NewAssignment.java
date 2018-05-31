package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.Activities.MainActivity;
import com.example.ammar.teacherandmev10.IdentifierClasses.Assignments;
import com.example.ammar.teacherandmev10.IdentifierClasses.Course;
import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Ammar on 2017-07-05.
 */

public class NewAssignment extends Fragment
{
    //TODO duedate and setdate have to be in chronological order
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private EditText assignmentNameInput;
    private EditText assignmentWeightInput;
    private EditText assignedDateInput;
    private EditText dueDateInput;
    private CalendarView calendar;
    private TextView assignmentNameTitle;
    private TextView assignmentWeightTitle;
    private TextView assignmentAssignedDateTitle;
    private TextView assignmentDueDateTitle;
    private Button newAssignment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View myView = inflater.inflate(R.layout.new_assignment,container,false);

        assignmentNameInput = (EditText) myView.findViewById(R.id.assignmentNameInput);
        assignmentWeightInput = (EditText) myView.findViewById(R.id.assignmentPercentageInput);
        assignedDateInput = (EditText) myView.findViewById(R.id.assignedDateInput);
        dueDateInput = (EditText) myView.findViewById(R.id.dueDateInput);
        calendar = (CalendarView) myView.findViewById(R.id.calendarViewAssignment);

        assignmentNameTitle = (TextView) myView.findViewById(R.id.assignmentNameText);
        assignmentWeightTitle = (TextView) myView.findViewById(R.id.assignmentPercentage);
        assignmentAssignedDateTitle = (TextView) myView.findViewById(R.id.assignedDate);
        assignmentDueDateTitle = (TextView) myView.findViewById(R.id.dueDate);
        newAssignment = (Button) myView.findViewById(R.id.addAssignmentButton);


        calendar.setVisibility(View.INVISIBLE);

        Date today = new Date();
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
        assignedDateInput.setText(todaysDate);

       assignedDateInput.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {

               calendar.setVisibility(View.VISIBLE);
               closeMenu();
                hideFields();

               calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                   @Override
                   public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
                   {
                       String toAdd;
                       if(month < 9)
                           toAdd = String.valueOf(year)+"-0" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);
                       else
                           toAdd = String.valueOf(year)+"-" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);

                       showFields(toAdd,"assigned");

                   }
               });
           }
       });

        dueDateInput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calendar.setVisibility(View.VISIBLE);
                closeMenu();
                hideFields();

                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)

                    {
                        String toAdd;
                        if(month < 9)
                            toAdd = String.valueOf(year)+"-0" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);
                        else
                            toAdd = String.valueOf(year)+"-" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);

                        showFields(toAdd,"due");
                    }
                });
            }
        });


        String courseName = getActivity().getIntent().getStringExtra("courseName");
        final DatabaseReference currentCourse = dbAccessFunctions.getChildOfCourses(courseName); //db is now equal to the current course, ie math etc

        newAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;

                if (assignedDateInput.getText().length() == 0 || assignmentWeightInput.getText().length() == 0
                        || assignedDateInput.getText().length() == 0 || dueDateInput.getText().length() == 0)
                {
                    check = false;
                    Toast.makeText(getActivity(), R.string.fields_empty,
                            Toast.LENGTH_SHORT).show();
                }

                if (check)
                {
                    final Assignments toUpload = new Assignments();
                  //  toUpload.setName(assignmentNameInput.getText().toString());
                    toUpload.setWeight(Double.parseDouble(assignmentWeightInput.getText().toString()));
                    toUpload.setAssignedDate(assignedDateInput.getText().toString());
                    toUpload.setDueDate(dueDateInput.getText().toString());


                currentCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        currentCourse.child("assignments").child(assignmentNameInput.getText().toString()).setValue(toUpload);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                currentCourse.child("classList").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        setAssignments(currentCourse.child("classList"),iterator,assignmentNameInput.getText().toString(),Double.parseDouble(assignmentWeightInput.getText().toString()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                getFragmentManager().beginTransaction().replace(R.id.content_frame, new DrawerAssignments()).commit();
            }
            }
        });

        return myView;
    }

    private void setAssignments (final DatabaseReference classList,Iterator<DataSnapshot> it, final String name,Double weight)
    {
        while (it.hasNext())
        {
            HashMap<String,Object> toUpload = new HashMap<>();
            toUpload.put("Grade",0);
            toUpload.put("weight (%)",weight);
            toUpload.put("submitted", false);
            classList.child(it.next().getKey()).child("assignments").child(name).updateChildren(toUpload);
        }
    }

    private void closeMenu()
    {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void hideFields()
    {
        assignmentNameInput.setAlpha(0);
        assignmentWeightInput.setAlpha(0);
        dueDateInput.setAlpha(0);
        assignedDateInput.setAlpha(0);
        assignmentNameTitle.setAlpha(0);
        assignmentWeightTitle.setAlpha(0);
        assignmentAssignedDateTitle.setAlpha(0);
        assignmentDueDateTitle.setAlpha(0);
        newAssignment.setAlpha(0);
    }

    private void showFields(String input, String due)
    {
        if(due.equals("due"))
        {
            dueDateInput.setText(input);
        }
        else
        {
            assignedDateInput.setText(input);

        }
        calendar.setVisibility(View.INVISIBLE);
        assignmentNameInput.setAlpha(1);
        assignmentWeightInput.setAlpha(1);
        assignedDateInput.setAlpha(1);
        dueDateInput.setAlpha(1);
        assignmentNameTitle.setAlpha(1);
        assignmentWeightTitle.setAlpha(1);
        assignmentAssignedDateTitle.setAlpha(1);
        assignmentDueDateTitle.setAlpha(1);
        newAssignment.setAlpha(1);
    }


//TODO consider the situation when a student is added but there are already existing assignments
}
