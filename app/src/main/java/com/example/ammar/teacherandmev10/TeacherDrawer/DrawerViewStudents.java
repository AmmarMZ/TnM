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

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
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

    private static DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private NavigationView navigationView;
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
        if (studentList.getAdapter() == null)
        {
            message.setVisibility(View.VISIBLE);
        }
        final Object objReceived = getActivity().getIntent().getStringExtra("courseName");
        String courseName = (String) objReceived;
        DatabaseReference classList = dbAccessFunctions.getClassList(courseName);

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        classList.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Menu menu = navigationView.getMenu();
                int val = 0;
                MenuItem item;

                for (int i = 0; i < menu.size(); i++)
                {
                    item = menu.getItem(i);
                    if (item.isChecked())
                    {
                        val = i;
                        break;
                    }
                }
                item = menu.getItem(val);

                String title = item.getTitle().toString().trim();

                if (title.equals("View Student List") || !item.isChecked())
                {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    String[] sLA = dbAccessFunctions.getChildrenOfDatabaseKeys(iterator);
                    adapter = new CustomAdapter(getActivity(), sLA, new int[sLA.length], new String[sLA.length],myView.getContext(),null);
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

        enrollButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().beginTransaction().replace(R.id.content_frame,new DrawerEnrollStudents()).addToBackStack(null).commit();
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
}
