package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ammar on 2017-05-21.
 */

//this is a shit show...
    //used for attendance and only attendance

public class CustomAdapter extends BaseAdapter {

    private NavigationView navigationView;

    String [] result;
    //names of students

    String [] status;
    //status is used in the attendance ListView, values are Present,Absent,Sick,Other

    private Context context;
    int [] imageId;
    //the colour used in the attendance listView, corresponds to present,absent,sick,other

    public static Activity activity;
    //current activity
    String date;
    //yyyy-mm-dd

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private static LayoutInflater inflater;

    public CustomAdapter(Activity mainActivity, String[] prgmNameList, int[] prgmImages,String [] stats,Context con)
    {
        result = prgmNameList;
        status = stats;
        context = mainActivity;
        imageId = prgmImages;
        context = con;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView studentName;
        TextView status;
        ImageView colour;
        ImageButton dots;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_attendance_list, null);

        holder.studentName=(TextView) rowView.findViewById(R.id.studentNameText);
        holder.status=(TextView) rowView.findViewById(R.id.pres_abs_sick);
        holder.colour =(ImageView) rowView.findViewById(R.id.imageView1);

        holder.studentName.setText(result[position]);
        holder.colour.setImageResource(imageId[position]);
        holder.status.setText(status[position]);

        holder.dots =(ImageButton) rowView.findViewById(R.id.imageButton4);

        final String extracted = result[position].trim(); //name of student

        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
        final DatabaseReference db = (DatabaseReference) objReceived;
        final DatabaseReference databaseReference = db.child(extracted).child("attendance");
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        holder.dots.setOnClickListener(new View.OnClickListener() { //change
            @Override
            public void onClick(View v)
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
                        final String extracted = result[position];
                        PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                        popupMenu.getMenu().add(R.string.remove_student_pop);
                        popupMenu.show();

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick(MenuItem item)
                            {
                                removeStudent(extracted);
                                return true;
                            }
                        });
                    }
                    if (title.equals("Attendance"))
                    {
//                        final String extracted = result[position].trim(); //name of student
                        final PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                        popupMenu.getMenu().add(R.string.present);
                        popupMenu.getMenu().add(R.string.sick);
                        popupMenu.getMenu().add(R.string.absent);
                        popupMenu.getMenu().add(R.string.other);
                        popupMenu.show();

//                        final Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
//                        final DatabaseReference db = (DatabaseReference) objReceived;
//                        final DatabaseReference databaseReference = db.child(extracted).child("attendance");

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick(final MenuItem item)
                            {

                                        if (popupMenu.getMenu().getItem(0).equals(item)) //present
                                        {
                                            databaseReference.child(getDate()).removeValue();
                                            databaseReference.child(getDate()).setValue("Present");
                                        }
                                        if (popupMenu.getMenu().getItem(1).equals(item)) //sick
                                        {
                                            databaseReference.child(getDate()).removeValue();
                                            databaseReference.child(getDate()).setValue("Sick");
                                        }
                                        if (popupMenu.getMenu().getItem(2).equals(item))//absent
                                        {
                                            databaseReference.child(getDate()).setValue("Absent");

                                        }
                                        if (popupMenu.getMenu().getItem(3).equals(item))//other
                                        {
                                            databaseReference.child(getDate()).setValue("Other");
                                        }



                                return true;
                            }
                        });
                    }

            }
        });
        return rowView;
    }

    public void removeStudent(final String student)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_student)
                .setMessage(R.string.delete_student)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                    DatabaseReference db = (DatabaseReference) objReceived;
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.child(student).removeValue();
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
