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

public class DrawerExams extends Fragment { //firstLayout xml

    View myView;
    private CustomAdapterAQTE examAdapter;
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.drawer_exams,container,false);

        final ListView examView = (ListView) myView.findViewById(R.id.examsListView);
        Button newExam = (Button) myView.findViewById(R.id.button15);

        newExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DynamicCourseView.fm.beginTransaction().replace(R.id.content_frame, new NewQuizTestExam()).addToBackStack("newExam").commit();
            }
        });

        String courseName = getActivity().getIntent().getStringExtra("courseName");
        DatabaseReference exams = dbAccessFunctions.getExams(courseName);

        exams.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterator<DataSnapshot> iterator1 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = dataSnapshot.getChildren().iterator();

                ArrayList<String> names = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator1,new ArrayList<String>());
                ArrayList<String> assignedDate = dbAccessFunctions.getChildrenOfDatabaseValues(iterator2,new ArrayList<String>(),"assignedDate");
                ArrayList<String> dueDate = dbAccessFunctions.getChildrenOfDatabaseValues(iterator3,new ArrayList<String>(), "dueDate");

                String [] quizNames = names.toArray(new String[0]);
                String [] assDate = assignedDate.toArray(new String[0]);
                String [] dDate = dueDate.toArray(new String[0]);

                examAdapter = new CustomAdapterAQTE(getActivity(),quizNames,myView.getContext(),assDate,dDate);
                examView.setAdapter(examAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        return myView;
    }
}
