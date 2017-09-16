package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.ammar.teacherandmev10.R;

/**
 * Created by Ammar on 2017-05-20.
 */

public class DrawerViewAttendance extends Fragment { //firstLayout xml

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.drawer_view_attendance,container,false);
        final android.app.FragmentManager fragmentManager = getFragmentManager();


        CalendarView calendarView = (CalendarView) myView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                Fragment next = new DrawerCustomAttendanceList();
                Bundle date = new Bundle();
                month = month + 1;
                date.putInt("year",year);
                date.putInt("month",month);
                date.putInt("day",dayOfMonth);
                next.setArguments(date);

                fragmentManager.beginTransaction().replace(R.id.content_frame,next).commit();

            }
        });


        return myView;
    }
}
