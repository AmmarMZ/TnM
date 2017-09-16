package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.ammar.teacherandmev10.IdentifierClasses.Assignments;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.util.Calendar.DATE;

/**
 * Created by Ammar on 2017-05-20.
 */

public class DrawerAssignments extends Fragment { //firstLayout xml

    View myView;
    private CustomAdapterAQTE adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.drawer_assignments,container,false);

        final ListView assignmentList = (ListView) myView.findViewById(R.id.assignmentListView);

        final Button newAssignment = (Button) myView.findViewById(R.id.button12);
        newAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicCourseView.fm.beginTransaction().replace(R.id.content_frame, new NewAssignment()).addToBackStack(null).commit();
            }
        });

        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        DatabaseReference db = (DatabaseReference) objReceived; //classList
        db = db.getParent().child("assignments");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DataSnapshot d1 = dataSnapshot;
                DataSnapshot d2 = dataSnapshot;
                DataSnapshot d3 = dataSnapshot;

                Iterator<DataSnapshot> iterator1 = d1.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = d2.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = d3.getChildren().iterator();

                ArrayList<String> assNames = getAssignment(iterator1);
                ArrayList<String> assignedDate = getAssignedDate(iterator2);
                ArrayList<String> dueDate = getDueDate(iterator3);

                String [] assignments = assNames.toArray(new String[0]);
                String [] assDate = assignedDate.toArray(new String[0]);
                String [] dDate = dueDate.toArray(new String[0]);

                adapter = new CustomAdapterAQTE(getActivity(),assignments,myView.getContext(),assDate,dDate);
                assignmentList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return myView;
    }
    private ArrayList<String> getAssignment(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> assignmentName = new ArrayList<>();

        while (iterator.hasNext())
        {
            assignmentName.add(iterator.next().getKey());
        }
        return assignmentName;
    }

    private ArrayList<String> getAssignedDate(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> assignedDates = new ArrayList<>();

        while (iterator.hasNext())
        {
           assignedDates.add((String) iterator.next().child("assignedDate").getValue());
        }
        return assignedDates;
    }

    private ArrayList<String> getDueDate(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> dueDates = new ArrayList<>();

        while (iterator.hasNext())
        {
            dueDates.add((String) iterator.next().child("dueDate").getValue());
        }
        return dueDates;
    }


}
