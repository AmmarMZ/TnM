package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.Activities.MainActivity;
import com.example.ammar.teacherandmev10.IdentifierClasses.Assignments;
import com.example.ammar.teacherandmev10.IdentifierClasses.Course;
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

//TODO when the calendar view appears and you click back it takes you to the assignment list menu instead of the new assignment page
public class NewAssignment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View myView = inflater.inflate(R.layout.new_assignment,container,false);

        final EditText assignmentNameInput = (EditText) myView.findViewById(R.id.assignmentNameInput);
        final EditText assignmentWeightInput = (EditText) myView.findViewById(R.id.assignmentPercentageInput);
        final EditText assignedDateInput = (EditText) myView.findViewById(R.id.assignedDateInput);
        final EditText dueDateInput = (EditText) myView.findViewById(R.id.dueDateInput);
        final CalendarView calendar = (CalendarView) myView.findViewById(R.id.calendarViewAssignment);

        final TextView assignmentNameTitle = (TextView) myView.findViewById(R.id.assignmentNameText);
        final TextView assignmentWeightTitle = (TextView) myView.findViewById(R.id.assignmentPercentage);
        final TextView assignmentAssignedDateTitle = (TextView) myView.findViewById(R.id.assignedDate);
        final TextView assignmentDueDateTitle = (TextView) myView.findViewById(R.id.dueDate);

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

               assignmentNameInput.setAlpha(0);
               assignmentWeightInput.setAlpha(0);
               dueDateInput.setAlpha(0);
               assignedDateInput.setAlpha(0);
               assignmentNameTitle.setAlpha(0);
               assignmentWeightTitle.setAlpha(0);
               assignmentAssignedDateTitle.setAlpha(0);
               assignmentDueDateTitle.setAlpha(0);

               calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                   @Override
                   public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
                   {
                       String toAdd;
                       if(month < 9)
                       toAdd = String.valueOf(year)+"-0" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);
                       else
                           toAdd = String.valueOf(year)+"-" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);

                       assignedDateInput.setText(toAdd);
                       calendar.setVisibility(View.INVISIBLE);
                       assignmentNameInput.setAlpha(1);
                       assignmentWeightInput.setAlpha(1);
                       assignedDateInput.setAlpha(1);
                       dueDateInput.setAlpha(1);
                       assignmentNameTitle.setAlpha(1);
                       assignmentWeightTitle.setAlpha(1);
                       assignmentAssignedDateTitle.setAlpha(1);
                       assignmentDueDateTitle.setAlpha(1);


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

                assignmentNameInput.setAlpha(0);
                assignmentWeightInput.setAlpha(0);
                assignedDateInput.setAlpha(0);
                dueDateInput.setAlpha(0);
                assignmentNameTitle.setAlpha(0);
                assignmentWeightTitle.setAlpha(0);
                assignmentAssignedDateTitle.setAlpha(0);
                assignmentDueDateTitle.setAlpha(0);

                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)

                    {
                        String toAdd;
                        if(month < 9)
                            toAdd = String.valueOf(year)+"-0" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);
                        else
                            toAdd = String.valueOf(year)+"-" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);

                        dueDateInput.setText(toAdd);
                        calendar.setVisibility(View.INVISIBLE);
                        assignmentNameInput.setAlpha(1);
                        assignmentWeightInput.setAlpha(1);
                        assignedDateInput.setAlpha(1);
                        dueDateInput.setAlpha(1);
                        assignmentNameTitle.setAlpha(1);
                        assignmentWeightTitle.setAlpha(1);
                        assignmentAssignedDateTitle.setAlpha(1);
                        assignmentDueDateTitle.setAlpha(1);


                    }
                });
            }
        });


        Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        DatabaseReference db = (DatabaseReference) objReceived;
        final DatabaseReference currentCourse = db.getParent(); //db is now equal to the current course, ie math etc

        Button newAssignment = (Button) myView.findViewById(R.id.addAssignmentButton);

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


                currentCourse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        currentCourse.child("assignments").child(assignmentNameInput.getText().toString()).setValue(toUpload);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                currentCourse.child("classList").addValueEventListener(new ValueEventListener()
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
            toUpload.put(name+"- Grade",0);
            toUpload.put("weight (%)",weight);
            classList.child(it.next().getKey()).child("assignments").child(name).updateChildren(toUpload);
        }
    }
//TODO - NOT Really a todo but consider the situation when a student is added but there are already existing assignments
}
