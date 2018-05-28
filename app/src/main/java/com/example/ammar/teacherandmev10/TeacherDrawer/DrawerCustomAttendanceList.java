package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.IdentifierClasses.Student;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ammar on 2017-05-21.
 */
public class DrawerCustomAttendanceList extends Fragment
{
    private static View myView;
    private CustomAdapter adapter; //adapter to set: colour - name - status - 3 dot menu
    private String realDate;    //date in yyyy-mm-dd
    private NavigationView navigationView; //the drawer menu
    private TextView titleDate;
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private DatabaseReference classList;
    private String [] namesInput;
    private String [] statusInput;
    private int [] colours;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.custom_list_view,container,false);
        context = getActivity().getApplicationContext();

        String courseName = getActivity().getIntent().getStringExtra("courseName");
        classList = dbAccessFunctions.getClassList(courseName);

        final ListView attendanceList = (ListView) myView.findViewById(R.id.listViewAttendance);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        titleDate = (TextView) myView.findViewById(R.id.textView1);

        Bundle date = getArguments();
        int year = date.getInt("year");
        int month = date.getInt("month");
        int day = date.getInt("day");
        final String studentName = date.getString("studentName");

        if (month < 10 && day < 10)
            realDate = Integer.toString(year) + "-0" + Integer.toString(month) + "-0" + Integer.toString(day);
        if(month < 10 && day > 10)
            realDate = Integer.toString(year) + "-0" + Integer.toString(month) + "-" + Integer.toString(day);
        if(month > 10 && day < 10)
            realDate = Integer.toString(year) + "-" + Integer.toString(month) + "-0" + Integer.toString(day);
        if(month > 10 && day > 10)
            realDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

        realDate = realDate.trim();

        titleDate.setText("Attendance for:        " + realDate);
        classList.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = dataSnapshot.getChildren().iterator();
                namesInput = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator);
                statusInput = getAttendance(iterator2);
                colours = setColours(statusInput);
                adapter = new CustomAdapter(getActivity(),namesInput,colours,statusInput,context,realDate,studentName);
                attendanceList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                System.out.println("Failed to read: " + databaseError.getCode());
            }
        });

        Button takeAttendanceButton = (Button) myView.findViewById(R.id.button11); //take attendance button

        takeAttendanceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(myView.getContext())
                        .setTitle(R.string.over_write_attendance)
                        .setMessage(R.string.over_write_attendance_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {


                            public void onClick(DialogInterface dialog, int which)
                            {
                                classList.addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                        setAttendance(iterator, classList);
                                        adapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}// do nothing
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return myView;
    }


    public int[] setColours(String [] statusInput)
    {
        int [] colours = new int [statusInput.length];
        for (int i = 0; i < statusInput.length; i++)
        {
            if (statusInput[i] == null || statusInput[i].equals("Other"))
            {
                colours[i] = R.color.colorPrimary;
                statusInput[i] = "Not Specified";
            }
            else if (statusInput[i].equals("Present"))
            {
                colours[i] = R.color.PresentGreen;
                statusInput[i] = "Present";
            }
            else if (statusInput[i].equals("Sick"))
            {
                colours[i] = R.color.SickYellow;
                statusInput[i] = "Sick";

            }
            else if (statusInput[i].equals("Absent"))
            {
                colours[i] = R.color.AbsentRed;
                statusInput[i] = "Absent";
            }
        }
        return colours;
    }

    public void setAttendance(Iterator<DataSnapshot> iterator, DatabaseReference db)
    {
        while (iterator.hasNext())
        {
            db.child(iterator.next().getKey()).child("attendance").child(realDate).setValue("Present");// updateChildren(attendanceToday);
        }
    }

    public String [] getAttendance(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> temp = new ArrayList<>();
        while (iterator.hasNext())
        {
            temp.add((String) iterator.next().child("attendance").child(realDate).getValue());
        }

        return temp.toArray(new String[0]);
    }
}
