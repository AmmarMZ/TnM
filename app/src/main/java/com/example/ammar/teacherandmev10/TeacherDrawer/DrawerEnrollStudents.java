package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.IdentifierClasses.Student;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;



/**
 * Created by Ammar on 2017-05-20.
 */

public class DrawerEnrollStudents extends Fragment //firstLayout xml
{
    private View myView;
    private DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.drawer_enroll_students,container,false);
        Button enroll = (Button) myView.findViewById(R.id.button9);
        final EditText input = (EditText) myView.findViewById(R.id.editText12);
        final NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        enroll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input.getText().toString().length() < 1)
                {
                    Toast.makeText(myView.getContext(), R.string.no_input_student, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String[] lines = input.getText().toString().split(System.getProperty("line.separator"));
                    String tempName, fName, lName;

                    String courseName = getActivity().getIntent().getStringExtra("courseName");
                    DatabaseReference classList = dbAccessFunctions.getClassList(courseName);

                    final Map<String, Object> studentsToAdd = new HashMap<>();
                    boolean check = true;
                    for (int i = 0; i < lines.length; i++)
                    {
                        check = true;
                        tempName = lines[i].trim();
                        if (tempName.indexOf(" ") > 0 && tempName.indexOf(" ") <= tempName.length())
                        {
                            fName = tempName.substring(0, tempName.indexOf(" "));
                            lName = tempName.substring(tempName.indexOf(" ")+1, tempName.length());
                            if (!Pattern.matches("[a-zA-Z]+", fName))
                            {
                                check = false;
                            }
                            if (!Pattern.matches("[a-zA-Z]+", lName))
                            {
                                check = false;
                            }
                            if (check)
                            {
                                Student newStud = new Student(fName,lName);
                                HashMap<String,String> tempAttendance = new HashMap<>();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                                Date today = new Date();
                                tempAttendance.put(dateFormat.format(today),Student.attendanceStatus.Present.toString());
                                newStud.setAttendance(tempAttendance);

                                //TODO implement behaviour map functionality - not important
                                HashMap<String,Student.Behaviour> behaviourMap = new HashMap<>();
                                behaviourMap.put(dateFormat.format(today), Student.Behaviour.Green);
                                newStud.setBehaviourMap(behaviourMap);

                                String uniqueId = UUID.randomUUID().toString();
                                newStud.setUniqueID(uniqueId);
                                studentsToAdd.put(uniqueId, newStud);
                            }
                        }
                        else
                        {
                            Toast.makeText(myView.getContext(), R.string.student_space_missing, Toast.LENGTH_SHORT).show();
                            check = false;
                        }

                    }
                    if (check)
                    {
                        classList.updateChildren(studentsToAdd);
                        Toast.makeText(myView.getContext(), R.string.student_succ_add, Toast.LENGTH_SHORT).show();
                        input.setText("");
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                    }
                }
            });
        return myView;
    }
}
