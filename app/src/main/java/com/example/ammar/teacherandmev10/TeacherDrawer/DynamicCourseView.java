package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.Activities.TeacherView;
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
    protected void onCreate(Bundle savedInstanceState)
    {
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
        navigationView.getMenu().getItem(1).setChecked(true);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new DrawerViewStudents()).addToBackStack(null).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,TeacherView.class);
        if(fm.getBackStackEntryCount() == 1)
        {
            super.startActivity(intent);
        }
        if (fm.getBackStackEntryCount() > 1)
        {
            fm.popBackStackImmediate();
        }
        else
        {
            super.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        String title = getIntent().getStringExtra("courseName");
        getSupportActionBar().setTitle(title + " - Student List");
        TextView drawerTitle = (TextView)findViewById(R.id.drawerTitle);
        drawerTitle.setText(title);

        TextView drawerDescription = (TextView)findViewById(R.id.drawerDescription);
        drawerDescription.setText("Navigation Drawer");

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        getMenuInflater().inflate(R.menu.dynam_course_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.action_settings)
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new CourseSettings()).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       // android.app.FragmentManager fm = getFragmentManager();
        String title = getIntent().getStringExtra("courseName");

        if (id == R.id.nav_first_layout)
        { //enroll Students
            setActionBarTitle(title + " - Enroll Students");
            fm.beginTransaction().replace(R.id.content_frame,new DrawerEnrollStudents()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_second_layout)
        {//view students[]''''''''''''''''''
            setActionBarTitle(title + " - Student List");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerViewStudents()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_fourth_layout)
        { //view attendance
            setActionBarTitle(title + " - Class Attendance");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerViewAttendance()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_fifth_layout)
        { //class assignments
            getSupportActionBar().setTitle(title + " - Class Assignments");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerAssignments()).addToBackStack("assignments").commit();
        }
        else if (id == R.id.nav_sixth_layout)
        { //class quizzes
            setActionBarTitle(title + " - Class Quizzes");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerQuizzes()).addToBackStack("quizzes").commit();
        }
        else if (id == R.id.nav_seventh_layout)
        { //class tests
            setActionBarTitle(title + " - Class Tests");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerTests()).addToBackStack("tests").commit();
        }
        else if (id == R.id.nav_eigth_layout)
        { //class exams
            setActionBarTitle(title + " - Class Exams");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerExams()).addToBackStack("exams").commit();
        }
        else if (id == R.id.nav_ninth_layout)
        { //class stats
            setActionBarTitle(title + " - Class Statistics");
            fm.beginTransaction().replace(R.id.content_frame, new DrawerStatistics()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
