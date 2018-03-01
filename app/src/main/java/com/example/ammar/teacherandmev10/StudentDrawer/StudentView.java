package com.example.ammar.teacherandmev10.StudentDrawer;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ammar.teacherandmev10.Activities.TeacherView;
import com.example.ammar.teacherandmev10.R;
import com.example.ammar.teacherandmev10.TeacherDrawer.DrawerEnrollStudents;
import com.example.ammar.teacherandmev10.TeacherDrawer.DrawerViewStudents;
import com.example.ammar.teacherandmev10.TeacherDrawer.DynamicCourseView;
//TODO investigate why app crashed when adding new exam to db after erasing all course data
public class StudentView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static FragmentManager fm;
    private static String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_student);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getFragmentManager();

        courseName = getIntent().getStringExtra("courseName");

        fm.beginTransaction().replace(R.id.content_frame_student, new StudentAssignments()).addToBackStack(null).commit();
        fm.executePendingTransactions();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,DynamicCourseView.class);
        intent.putExtra("courseName",courseName);

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
        getMenuInflater().inflate(R.menu.student_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.stud_nav_assignments)
        {
            getIntent().putExtra("AQTE","assignments");
            fm.beginTransaction().replace(R.id.content_frame_student,new StudentAssignments()).addToBackStack(null).commit();
        }
        else if (id == R.id.stud_nav_quizzes)
        {
            getIntent().putExtra("AQTE","quizzes");
            fm.beginTransaction().replace(R.id.content_frame_student,new StudentAssignments()).addToBackStack(null).commit();
        }
        else if (id == R.id.stud_nav_tests)
        {
            getIntent().putExtra("AQTE","tests");
            fm.beginTransaction().replace(R.id.content_frame_student,new StudentAssignments()).addToBackStack(null).commit();
        }
        else if (id == R.id.stud_nav_exams)
        {
            getIntent().putExtra("AQTE","exams");
            fm.beginTransaction().replace(R.id.content_frame_student,new StudentAssignments()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_student);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
