package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.IdentifierClasses.Quizzes;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Ammar on 2017-09-05.
 */

public class NewQuizTestExam extends Fragment
{
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View myView = inflater.inflate(R.layout.new_quiz_test_exam,container,false);

        final EditText nameInput = (EditText) myView.findViewById(R.id.quizTestExamNameInput);
        final EditText weightInput = (EditText) myView.findViewById(R.id.quizTestExamWeightInput);
        final EditText dateInput = (EditText) myView.findViewById(R.id.quizTestExamDateInput);

        final TextView nameText = (TextView) myView.findViewById(R.id.quizTestExamNameText);
        final TextView weightText = (TextView) myView.findViewById(R.id.quizTestExamWeightText);
        final TextView dateText = (TextView) myView.findViewById(R.id.quizTestExamDateText);

        final CalendarView quizTestExamCalendar = (CalendarView) myView.findViewById(R.id.quizTestExamCalendar);
        final Button createQuiz = (Button) myView.findViewById(R.id.quizTestExamButton);
        quizTestExamCalendar.setVisibility(View.INVISIBLE);

        Date today = new Date();
        final String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(today);

        dateInput.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                quizTestExamCalendar.setVisibility(View.VISIBLE);

                createQuiz.setAlpha(0);
                nameInput.setAlpha(0);
                weightInput.setAlpha(0);
                dateInput.setAlpha(0);
                nameText.setAlpha(0);
                weightText.setAlpha(0);
                dateText.setAlpha(0);

                quizTestExamCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
                {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
                    {
                        String toAdd;
                        if(month < 9)
                            toAdd = String.valueOf(year)+"-0" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);
                        else
                            toAdd = String.valueOf(year)+"-" + String.valueOf(month +1) + "-" + String.valueOf(dayOfMonth);

                        dateInput.setText(toAdd);
                        quizTestExamCalendar.setVisibility(View.INVISIBLE);

                        nameInput.setAlpha(1);
                        weightInput.setAlpha(1);
                        dateInput.setAlpha(1);
                        nameText.setAlpha(1);
                        weightText.setAlpha(1);
                        dateText.setAlpha(1);
                        createQuiz.setAlpha(1);

                    }
                });
            }
        });

        Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        DatabaseReference db = (DatabaseReference) objReceived;
        final DatabaseReference currentCourse = db.getParent(); //db is now equal to the current course, ie math etc

        createQuiz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                boolean check = true;

                if (nameInput.getText().length() == 0 || weightInput.getText().length() == 0
                        || dateInput.getText().length() == 0)
                {
                    check = false;
                    Toast.makeText(getActivity(), R.string.fields_empty,
                            Toast.LENGTH_SHORT).show();
                }
                if (check)
                {
                    final Quizzes toUpload = new Quizzes();
                   // toUpload.setName(nameInput.getText().toString());
                    toUpload.setWeight(Double.parseDouble(weightInput.getText().toString()));
                    toUpload.setAssignedDate(todaysDate);
                    toUpload.setDueDate(dateInput.getText().toString());

                    currentCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            currentCourse.child("quizzes").child(nameInput.getText().toString()).setValue(toUpload);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    currentCourse.child("classList").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            setQuizTestExam(currentCourse.child("classList"),iterator,nameInput.getText().toString(),Double.parseDouble(weightInput.getText().toString()));
                            weightInput.setText("");
                            dateInput.setText("");
                            nameInput.setText("");
                            Toast.makeText(getActivity(), R.string.quiz_succ_add,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return myView;
    }

    private void setQuizTestExam (final DatabaseReference classList,Iterator<DataSnapshot> it, final String name,Double weight)
    {
        while (it.hasNext())
        {
            HashMap<String,Object> toUpload = new HashMap<>();
            toUpload.put("Grade",0);
            toUpload.put("weight (%)",weight);
            classList.child(it.next().getKey()).child("quizzes").child(name).updateChildren(toUpload);
        }
    }


}