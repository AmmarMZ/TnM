package com.example.ammar.teacherandmev10.StudentDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.ammar.teacherandmev10.IdentifierClasses.Assignments;
import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ammar on 2018-01-18.
 */

public class StudentAssignments extends Fragment
{
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private static ExpandableListAdapter listAdapter;
    private static ExpandableListView expListView;
    private static List<String> listDataHeader;
    private static List<String> studentList = new ArrayList<>();
    private static HashMap<String, List<String>> listDataChild = new HashMap<>();

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.student_assignments, container, false);

        String studentName = getActivity().getIntent().getStringExtra("studentName");
        final String courseName = getActivity().getIntent().getStringExtra("courseName");
        final String AQTE = getActivity().getIntent().getStringExtra("AQTE");
        expListView = (ExpandableListView) myView.findViewById(R.id.studentAssignmentListView);

        final DatabaseReference studentAssignments = dbAccessFunctions.getAQTEofStudent(courseName,studentName, AQTE);

        studentAssignments.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final String assignmentNames [] = dbAccessFunctions.getChildrenOfDatabaseKeys(dataSnapshot.getChildren().iterator());
                listDataHeader =  arrayToListHeaders(assignmentNames);

                for (int i = 0; i < assignmentNames.length; i++)
                {
                    Iterator<DataSnapshot> iterator = dataSnapshot.child(assignmentNames[i]).getChildren().iterator();
                    List<String> list = new ArrayList<>();
                    int counter = 0;
                    while (iterator.hasNext())
                    {
                        Object temp = iterator.next().getValue();
                        if (counter == 0)
                        {
                            list.add("Grade: " + temp.toString());
                        }
                        if (AQTE.equals("assignments"))
                        {
                            if (counter == 1)
                            {
                                list.add("Handed In: " + temp.toString());
                            }
                            else if (counter > 1)
                            {
                                list.add("Weight (%): " + temp.toString());
                            }
                        }
                        else
                        {
                            if (counter == 1)
                            {
                                list.add("Weight (%): " + temp.toString());
                            }
                        }
                        counter++;
                    }
                    listDataChild.put(assignmentNames[i],list);
                }
                listAdapter = new ExpandableAQTECustomAdapter(myView.getContext(),listDataHeader,listDataChild, getActivity());
                expListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return myView;
    }

    private List<String> arrayToListHeaders(String [] headers)
    {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < headers.length; i ++)
        {
            list.add(headers[i]);
        }

        return list;
    }
}
