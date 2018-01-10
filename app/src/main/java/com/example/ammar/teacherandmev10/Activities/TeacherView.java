package com.example.ammar.teacherandmev10.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.ammar.teacherandmev10.IdentifierClasses.Course;
import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.R;
import com.example.ammar.teacherandmev10.TeacherDrawer.DynamicCourseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/*
    displays list of courses the teacher has in a non-custom listView using a non-custom adapter
    courses can be added and deleted by the teacher
 */
public class TeacherView extends AppCompatActivity
{
    private static ArrayList<String> courseList = new ArrayList<>();
    private static ArrayAdapter adapter;
    private DatabaseReference courses;
    private static DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);
        context = this;

        getSupportActionBar().setTitle(R.string.course_title);
        final ListView mListView = (ListView) findViewById(R.id.listView1);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.coursesProgressBar);

        courses = dbAccessFunctions.getCourses();
        courses.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                courseList = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator,courseList);
                adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, courseList);
                progressBar.setVisibility(View.GONE);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                
            }
        });

        final Intent intent = new Intent(this,DynamicCourseView.class);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String extracted = (String) mListView.getItemAtPosition(position);
                intent.putExtra("courseName",extracted);
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
            {
                final String extracted = (String) mListView.getItemAtPosition(pos);

                PopupMenu popupMenu = new PopupMenu(TeacherView.this,arg1);
                popupMenu.getMenu().add(R.string.remove_course);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() 
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Course temp = new Course();
                        temp.removeCourse(extracted, courses, context);
                        return true;
                    }
                });
                return true;
            }
        });
        
        Button addCourse = (Button) findViewById(R.id.button7);
        addCourse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Course newCourse = new Course();
                dbAccessFunctions.addChild(courses,newCourse,"course", context);
            }
        });
    }

}