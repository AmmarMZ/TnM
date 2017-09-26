package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Ammar on 2017-05-20.
 */

public class DrawerQuizzes extends Fragment { //firstLayout xml

    View myView;
    private CustomAdapterAQTE quizAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.drawer_quizzes,container,false);

        final ListView quizView = (ListView) myView.findViewById(R.id.quizzesListView);
        Button  newQuiz = (Button) myView.findViewById(R.id.button14);

        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DynamicCourseView.fm.beginTransaction().replace(R.id.content_frame, new NewQuizTestExam()).addToBackStack("newQuiz").commit();
            }
        });

        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        DatabaseReference db = (DatabaseReference) objReceived; //classList
        db = db.getParent().child("quizzes");

        db.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DataSnapshot d1 = dataSnapshot;
                DataSnapshot d2 = dataSnapshot;
                DataSnapshot d3 = dataSnapshot;

                Iterator<DataSnapshot> iterator1 = d1.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = d2.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = d3.getChildren().iterator();

                ArrayList<String> assNames = getQuizzes(iterator1);
                ArrayList<String> assignedDate = getAssignedDate(iterator2);
                ArrayList<String> dueDate = getDueDate(iterator3);

                String [] assignments = assNames.toArray(new String[0]);
                String [] assDate = assignedDate.toArray(new String[0]);
                String [] dDate = dueDate.toArray(new String[0]);

                quizAdapter = new CustomAdapterAQTE(getActivity(),assignments,myView.getContext(),assDate,dDate);
                quizView.setAdapter(quizAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myView;
    }

    private ArrayList<String> getQuizzes(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> quizNames = new ArrayList<>();

        while (iterator.hasNext())
        {
            quizNames.add(iterator.next().getKey());
        }
        return quizNames;
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
