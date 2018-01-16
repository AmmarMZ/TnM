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

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
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
    public CustomAdapterAQTE quizAdapter;
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.drawer_quizzes,container,false);

        if (quizAdapter != null)
        {
            quizAdapter = new CustomAdapterAQTE();
        }
        final ListView quizView = (ListView) myView.findViewById(R.id.quizzesListView);
        Button  newQuiz = (Button) myView.findViewById(R.id.button14);

        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DynamicCourseView.fm.beginTransaction().replace(R.id.content_frame, new NewQuizTestExam()).addToBackStack("newQuiz").commit();
            }
        });

        String courseName = getActivity().getIntent().getStringExtra("courseName");
        DatabaseReference quizzes = dbAccessFunctions.getQuizzes(courseName);

        quizzes.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                System.out.println("-------------------------QUIZZES-----------------------");
                System.out.println(dataSnapshot);
                System.out.println("-------------------------QUIZZES-----------------------");

                Iterator<DataSnapshot> iterator1 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator2 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> iterator3 = dataSnapshot.getChildren().iterator();

                String [] quizNames = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator1);
                String [] assDate = dbAccessFunctions.getChildrenOfDatabaseValues(iterator2,"assignedDate");
                String [] dDate = dbAccessFunctions.getChildrenOfDatabaseValues(iterator3,"dueDate");

                quizAdapter = new CustomAdapterAQTE(getActivity(),quizNames,myView.getContext(),assDate,dDate);
                quizView.setAdapter(quizAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        return myView;
    }
}
