package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
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

public class DrawerTests extends Fragment { //firstLayout xml

    View myView;
    private CustomAdapterAQTE testAdapter;
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.drawer_tests,container,false);

        if (testAdapter != null)
        {
            testAdapter = new CustomAdapterAQTE();
        }

        final ListView testView = (ListView) myView.findViewById(R.id.testListView);
        Button newQuiz = (Button) myView.findViewById(R.id.button13);

        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DynamicCourseView.fm.beginTransaction().replace(R.id.content_frame, new NewQuizTestExam()).addToBackStack("newTest").commit();
            }
        });

        String courseName = getActivity().getIntent().getStringExtra("courseName");
        DatabaseReference tests = dbAccessFunctions.getTests(courseName);

        tests.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                System.out.println("-------------------------TESTS-----------------------");
                System.out.println(dataSnapshot);
                System.out.println("-------------------------TESTS-----------------------");

                Iterator<DataSnapshot> iterator1 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = dataSnapshot.getChildren().iterator();

                String [] testNames  = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator1);
                String [] assDate = dbAccessFunctions.getChildrenOfDatabaseValues(iterator2,"assignedDate");
                String [] dDate  = dbAccessFunctions.getChildrenOfDatabaseValues(iterator3,"dueDate");

                testAdapter = new CustomAdapterAQTE(getActivity(),testNames,myView.getContext(),assDate,dDate);
                testView.setAdapter(testAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        return myView;
    }
}
