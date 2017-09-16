package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class DrawerViewStudents extends Fragment { //firstLayout xml

    private NavigationView navigationView;
    private ArrayList<String> studentListArray = new ArrayList<>();
    private CustomAdapter adapter;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.drawer_view_students,container,false);

        final ListView studentList = (ListView)  myView.findViewById(R.id.studentListView);
        final TextView message = (TextView) myView.findViewById(R.id.textView20);
        final Button enrollButton = (Button) myView.findViewById(R.id.button10);
        message.setVisibility(View.GONE);

        //listener to update ListView if the Students ever change
        if (studentList.getAdapter() == null) {
            message.setVisibility(View.VISIBLE);
        }
        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        DatabaseReference db = (DatabaseReference) objReceived;

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                
                Menu menu = navigationView.getMenu();
                int val = 0;
                MenuItem item;

                for (int i = 0; i < menu.size(); i++) {
                    item = menu.getItem(i);
                    if (item.isChecked()) {
                        val = i;
                        break;
                    }
                }
                item = menu.getItem(val);

                String title = item.getTitle().toString().trim();

                if (title.equals("View Student List") || !item.isChecked()) {

                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    studentListArray = getStudents(iterator);
                    String[] sLA = studentListArray.toArray(new String[0]);
                    adapter = new CustomAdapter(getActivity(), sLA, new int[sLA.length], new String[sLA.length],myView.getContext());
                    adapter.setActivity(getActivity());
                    studentList.setAdapter(adapter);
                }
                //get iterator for all students and then collect all their names in #getStudents
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        }); //end of listener

        //listener when wanting to enroll students
        final NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        enrollButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navigationView.getMenu().performIdentifierAction(R.id.nav_first_layout, 0);

            }
        });

        studentList.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                message.setVisibility(View.GONE); //message to check if classlist is empty or not
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

                if (studentList.getAdapter().getCount() == 0) {
                    message.setVisibility(View.VISIBLE);
                }
            }
        });
        return myView;
    }

    private ArrayList<String> getStudents(Iterator<DataSnapshot> iterator)
    {
        ArrayList<String> studentListArray = new ArrayList<>();

        while (iterator.hasNext())
        {
            studentListArray.add(iterator.next().getKey());
        }
        return studentListArray;
    }

}