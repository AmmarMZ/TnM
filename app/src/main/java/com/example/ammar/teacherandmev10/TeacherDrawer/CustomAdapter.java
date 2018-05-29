package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.example.ammar.teacherandmev10.StudentDrawer.StudentView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ammar on 2017-05-21.
 */

//this is a shit show...
    //used for attendance and only attendance

public class CustomAdapter extends BaseAdapter {

    private  static DatabaseAccessFunctions dbAccessFunctions = new DatabaseAccessFunctions();
    private NavigationView navigationView;
    private static String courseName;
    private static String studentName;
    private static String studentNameFromStudentView;

    //names of students
    String [] result;
    //status is used in the attendance ListView, values are Present,Absent,Sick,Other
    String [] status;
    private Context context;
    //the colour used in the attendance listView, corresponds to present,absent,sick,other
    int [] imageId;
    public static Activity activity;
    String date;

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

    public CustomAdapter(Activity mainActivity, String[] prgmNameList, int[] prgmImages,String [] stats,Context con, String todaysDate, String studentName)
    {
        studentNameFromStudentView = studentName;
        if (studentName != null)
        {
            int index = 0;
            for (int i = 0; i < prgmNameList.length; i++)
            {
                if (prgmNameList[i].equals(studentName))
                {
                    index = i;
                    String temp = prgmNameList[i];
                    prgmNameList = new String[1];
                    prgmNameList[0] = temp;
                    break;
                }
            }
            int tempId = prgmImages[index];
            prgmImages = new int [1];
            prgmImages[0] = tempId;

            String tempStat = stats[index];
            stats = new String [1];
            stats[0] = tempStat;

        }


            result = prgmNameList;
            status = stats;
            context = mainActivity;
            imageId = prgmImages;
            context = con;
            date = todaysDate;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
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
        courseName = getActivity().getIntent().getStringExtra("courseName");

        final DatabaseReference attendanceRef = dbAccessFunctions.getClassList(courseName);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MenuItem item = getMenuItem();
                String title = item.getTitle().toString().trim();

                if ((title.equals("View Student List")))
                {
                    Intent intent = new Intent(getActivity(),StudentView.class);
                    intent.putExtra("AQTE","assignments");
                    intent.putExtra("courseName",courseName);
                    intent.putExtra("studentName",result[position]);
                    getActivity().startActivity(intent);
                }
            }
        });

        //clicking the kabob menu
        holder.dots.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MenuItem item;
                String title = null;
                if (studentNameFromStudentView == null)
                {
                    item = getMenuItem();
                    title = item.getTitle().toString().trim();
                    studentName = result[position];
                }
                else
                {
                    title = "Attendance";
                    studentName = studentNameFromStudentView;
                }
                if (title.equals("View Student List"))
                {
                    final PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.remove_student_pop);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            removeStudent(studentName);
                            return true;
                        }
                    });

                }
                else if (title.equals("Attendance") || studentNameFromStudentView != null)
                {
                    final PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.present);
                    popupMenu.getMenu().add(R.string.sick);
                    popupMenu.getMenu().add(R.string.absent);
                    popupMenu.getMenu().add(R.string.other);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item)
                        {
                            //present
                            if (popupMenu.getMenu().getItem(0).equals(item))
                            {
                                updateAttendance(attendanceRef,"Present");
                            }
                            //sick
                            if (popupMenu.getMenu().getItem(1).equals(item))
                            {
                                updateAttendance(attendanceRef,"Sick");
                            }
                            //absent
                            if (popupMenu.getMenu().getItem(2).equals(item))
                            {
                                updateAttendance(attendanceRef,"Absent");
                            }
                            //other
                            if (popupMenu.getMenu().getItem(3).equals(item))
                            {
                                updateAttendance(attendanceRef,"Other");
                            }
                            return true;
                        }
                    });
                }
            }
        });
        return rowView;
    }

    public void updateAttendance(DatabaseReference db, String type)
    {
        db.child(studentName).child("attendance").child(getDate()).setValue(type);
        this.notifyDataSetChanged();
    }
    public void removeStudent(final String student)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_student)
                .setMessage(R.string.delete_student)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    DatabaseReference classList = dbAccessFunctions.getClassList(courseName);
                    public void onClick(DialogInterface dialog, int which)
                    {
                        classList.child(student).removeValue();
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

    public MenuItem getMenuItem()
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
         return menu.getItem(val);
    }
}
