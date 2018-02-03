package com.example.ammar.teacherandmev10.StudentDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ammar.teacherandmev10.R;

/**
 * Created by Ammar on 2018-01-18.
 */

public class StudentAssignments extends Fragment
{
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.student_assignments, container, false);

        String name = getActivity().getIntent().getStringExtra("studentName");

        TextView temp = (TextView) myView.findViewById(R.id.tempTextStudent);

        temp.setText(name);

        return myView;
    }
}
