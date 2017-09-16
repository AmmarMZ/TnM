package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ammar.teacherandmev10.R;

/*
    This activity is generated dynamically based on what course was clicked
    This is a navigation drawer activity meaning fragments are used
    The activity loads the student list and displays it on startup
    A CUSTOM ADAPTER IS USED HERE -> view CustomAdapter.java

 */



public class DynamicCourseView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynam_course_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fm = fragmentManager;
        fragmentManager.beginTransaction().replace(R.id.content_frame, new DrawerViewStudents()).addToBackStack(null).commit();
        fragmentManager.executePendingTransactions();



    }

    @Override
    public void onBackPressed()
    {
        if(fm.getBackStackEntryCount() > 0)
            fm.popBackStackImmediate();
        else
            super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        String title = getIntent().getStringExtra("courseName");
        getSupportActionBar().setTitle( title + " - Student List");
        TextView drawerTitle = (TextView)findViewById(R.id.drawerTitle);
        drawerTitle.setText(title);

        TextView drawerDescription = (TextView)findViewById(R.id.drawerDescription);
        drawerDescription.setText("Navigation Drawer");

        getMenuInflater().inflate(R.menu.dynam_course_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new CourseSettings()).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       // android.app.FragmentManager fm = getFragmentManager();
        String title = getIntent().getStringExtra("courseName");

        if (id == R.id.nav_first_layout)
        { //enroll Students
            getSupportActionBar().setTitle(title + " - Enroll Students");
            fm.beginTransaction().replace(R.id.content_frame,new DrawerEnrollStudents()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_second_layout)
        {//view students[]''''''''''''''''''
            getSupportActionBar().setTitle(title + " - Student List");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerViewStudents()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_fourth_layout)
        { //view attendance
            getSupportActionBar().setTitle(title + " - Class Attendance");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerViewAttendance()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_fifth_layout)
        { //class assignments
            getSupportActionBar().setTitle(title + " - Class Assignments");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerAssignments()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_sixth_layout)
        { //class quizzes
            getSupportActionBar().setTitle(title + " - Class Quizzes");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerQuizzes()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_seventh_layout)
        { //class tests
            getSupportActionBar().setTitle(title + " - Class Tests");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerTests()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_eigth_layout)
        { //class exams
            getSupportActionBar().setTitle(title + " - Class Exams");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerExams()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_ninth_layout)
        { //class stats
            getSupportActionBar().setTitle(title + " - Class Statistics");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerStatistics()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
