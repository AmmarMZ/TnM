package com.example.ammar.teacherandmev10.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ammar.teacherandmev10.IdentifierClasses.Course;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.example.ammar.teacherandmev10.TeacherDrawer.DynamicCourseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/*
    displays list of courses the teacher has in a non-custom listView using a non-custom adapter
    courses can be added and deleted by the teacher
 */
public class TeacherView extends AppCompatActivity
{
    private static ArrayList<String> courseList = new ArrayList<>();
    private ArrayAdapter adapter;
    private Object objReceived;
    private DatabaseReference db;
    private DatabaseReference courses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        getSupportActionBar().setTitle(R.string.course_title);
        //set title = Teacher and Me - Courses

        final ListView mListView = (ListView) findViewById(R.id.listView1);
        //get the listView

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList);
        //using basic arrayAdapter for this activity

        if(objReceived == null)
        objReceived = ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("dataBase")).getData();
        db = (DatabaseReference) objReceived;
        courses = db.child("courses");
        //get the db reference that was passed from the previous intent and set it currently to courses


        courses.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                getCourses(iterator);
                //fills static String Array with all courseNames
                //get iterator for all courses and then collect all their names in #getCourses
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //do nothing
            }
        });


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList);
        mListView.setAdapter(adapter);
        //set listView to all the courses

        final Intent intent = new Intent(this,DynamicCourseView.class);
        //intent for when you click on a course

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String extracted = (String) mListView.getItemAtPosition(position);
                //returns the name of the course that was clicked/touched

                DatabaseReference next = courses.child(extracted).child("classList");
                final Bundle bundle = new Bundle();
                bundle.putBinder("classList", new ObjectWrapperForBinder(next));
                intent.putExtra("courseName",extracted);
                //pass the current hierarchy to the next intent to make accessing database faster

                startActivity(intent.putExtras(bundle));
                //run the dynamicCourseView activity

            }
        });

        //holding long click on a course will give the option to delete the course
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
            {
                final String extracted = (String) mListView.getItemAtPosition(pos);

                PopupMenu popupMenu = new PopupMenu(TeacherView.this,arg1);
                popupMenu.getMenu().add(R.string.remove_course);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        removeCourse(extracted,db);
                        return true;
                    }
                });
                return true;
            }
        });


        Button addCourse = (Button) findViewById(R.id.button7);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse(courses);
            }
        });
    }

    //this function is tied to the addCourse button via XML
    //can view it in the res -> layout -> activity_teacher_view.xml
    //adds courses to the database and the listview is automatically updated to show new course
    public void addCourse(final DatabaseReference db) //called when enroll students is clicked
    {
        final Map<String,Object> courseToAdd = new HashMap<>();
        final Course newCourse = new Course();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a course");
        alert.setMessage("Enter the name of the course you'd like to add");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = input.getText().toString();
                courseToAdd.put(name,newCourse);
                courses.getRef().updateChildren(courseToAdd);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    //used to fill static array with the names of courses
    private void getCourses(Iterator<DataSnapshot> iterator)
    {
        if(courseList.size() > 0)
            courseList.clear();

        while (iterator.hasNext())
        {
            courseList.add(String.valueOf(iterator.next().getKey()));
        }
        adapter.notifyDataSetChanged();
    }

    //called after long clicking a course name
    //removes the course from the db and  but provides a warning, listView is automatically updated
    public void removeCourse(final String course, final DatabaseReference db)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_course)
                .setMessage(R.string.delete_course)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        courses.child(course).removeValue();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}