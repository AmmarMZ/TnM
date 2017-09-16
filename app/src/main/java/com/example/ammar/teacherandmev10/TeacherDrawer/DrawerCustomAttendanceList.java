package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
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

import com.example.ammar.teacherandmev10.IdentifierClasses.Student;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ammar on 2017-05-21.
 */
//TODO - Figure out why attendance statuses can't be changed after using the "take attendance button"
public class DrawerCustomAttendanceList extends Fragment
{
    private static View myView;
    private CustomAdapter adapter; //adapter to set: colour - name - status - 3 dot menu
    private String realDate;    //date in yyyy-mm-dd
    private NavigationView navigationView; //the drawer menu
    private TextView titleDate;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.custom_list_view,container,false);
        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        final DatabaseReference db = (DatabaseReference) objReceived; //classList
        final ListView attendanceList = (ListView) myView.findViewById(R.id.listViewAttendance);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);


        db.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (navigationView == null)
                navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

                Menu menu = navigationView.getMenu();
                int val = 0;
                MenuItem item;

                for (int i = 0; i < menu.size(); i++)
                {
                    item = menu.getItem(i);
                    if (item.isChecked())
                    {
                        val = i;
                        break;
                    }
                }
                item = menu.getItem(val);
                String title = item.getTitle().toString().trim();
                if (title.equals("Attendance"))
                {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Bundle date = getArguments();
                    int year = date.getInt("year");
                    int month = date.getInt("month");
                    int day = date.getInt("day");

                    if (month < 10 && day < 10)
                        realDate = Integer.toString(year) + "-0" + Integer.toString(month) + "-0" + Integer.toString(day);
                    if(month < 10 && day > 10)
                        realDate = Integer.toString(year) + "-0" + Integer.toString(month) + "-" + Integer.toString(day);
                    if(month > 10 && day < 10)
                        realDate = Integer.toString(year) + "-" + Integer.toString(month) + "-0" + Integer.toString(day);
                    if(month > 10 && day > 10)
                        realDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

                    realDate = realDate.trim();

                    if (titleDate == null)
                    titleDate = (TextView) getActivity().findViewById(R.id.textView1);

                    ArrayList<ArrayList> threeInputs = getStudents(iterator, realDate);
                    ArrayList<String> names = threeInputs.get(0);
                    ArrayList<Integer> colours = threeInputs.get(1);    //green,red,yellow,or blue
                    ArrayList<String> statuses = threeInputs.get(2);    //present,absent,sick, or other

                    titleDate.setText(realDate + "           " + names.size() + " Students");

                    String[] namesInput = names.toArray(new String[0]);
                    String[] statusesInput = statuses.toArray(new String[0]);
                    int[] coloursInput = new int[colours.size()];
                    for (int i = 0; i < colours.size(); i++)
                    {
                        coloursInput[i] = colours.get(i);
                    }

                    adapter = new CustomAdapter(getActivity(), namesInput, coloursInput, statusesInput,myView.getContext());
                    adapter.setActivity(getActivity());
                    adapter.setDate(realDate);
                    attendanceList.setAdapter(adapter);
                    //get iterator for all students and then collect all their names in #getStudents
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                System.out.println("Failed to read: " + databaseError.getCode());
            }
        }); //end of database listener

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
                            Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                            DatabaseReference databaseClassList = (DatabaseReference) objReceived;

                            public void onClick(DialogInterface dialog, int which)
                            {
                                databaseClassList.addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                        setAttendance(iterator, databaseClassList);
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
    private ArrayList<ArrayList> getStudents(Iterator<DataSnapshot> iterator, String date)
    {
        ArrayList<String> studentListArray = new ArrayList<>();
        ArrayList<Integer> studentColourArray = new ArrayList<>();
        ArrayList<String> studentStatusArray = new ArrayList<>();

        while (iterator.hasNext())
        {
            Student student = iterator.next().getValue(Student.class);
            studentListArray.add("  " + student.getFirstName() + " " + student.getLastName());

            HashMap<String,String> attendanceHash = student.getAttendance();

            if (attendanceHash.get(date) == null || attendanceHash.get(date).equals("Other"))
            {
                studentColourArray.add(R.color.colorPrimary);
                studentStatusArray.add("    Other");
            }
            else if (attendanceHash.get(date).equals("Present"))
            {
                studentColourArray.add(R.color.PresentGreen);
                studentStatusArray.add("    Present");
            }
            else if (attendanceHash.get(date).equals("Sick"))
            {
                studentColourArray.add(R.color.SickYellow);
                studentStatusArray.add("    Sick");
            }
            else if (attendanceHash.get(date).equals("Absent"))
            {
                studentColourArray.add(R.color.AbsentRed);
                studentStatusArray.add("    Absent");
            }
        }

        ArrayList<ArrayList> rVal = new ArrayList<>();
        rVal.add(studentListArray);
        rVal.add(studentColourArray);
        rVal.add(studentStatusArray);

        return rVal;
    }

    public void setAttendance(Iterator<DataSnapshot> iterator, DatabaseReference db)
    {
        while (iterator.hasNext())
        {
//            Map<String,Object> attendanceToday = new HashMap<>();
//            attendanceToday.put(realDate,"Present");
            db.child(iterator.next().getKey()).child("attendance").child(realDate).setValue("Present");// updateChildren(attendanceToday);

        }
    }

}
