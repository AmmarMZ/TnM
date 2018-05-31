package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.DatabaseAccessFunctions;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Ammar on 2017-06-11.
 */


public class CustomAdapterAQTE extends BaseAdapter
{
    private NavigationView navigationView;
    private static LayoutInflater inflater;
    private static String [] assignmentNameArray;
    private Context context;
    private Activity activity;
    private String[] assignedDate;
    private String[] dueDate;
    private Color [] colors;
    private DatabaseAccessFunctions dbAccessfunctions = new DatabaseAccessFunctions();

    public CustomAdapterAQTE()
    {}

    public CustomAdapterAQTE(Activity mainActivity, String[] assignmentNames, Context con, String[] assign, String [] due)
    {
        assignmentNameArray = assignmentNames;
        context = con;
        activity = mainActivity;
        assignedDate = assign;
        dueDate = due;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView assignmentName;
        ImageButton dots;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Holder holder =  new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_aqte_list, null);

        GradientDrawable gradientDrawable = (GradientDrawable) rowView.getBackground();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String assigned = assignedDate[position];
        String due = dueDate[position];

        holder.assignmentName = (TextView) rowView.findViewById(R.id.assignmentNameText);
        holder.dots = (ImageButton) rowView.findViewById(R.id.imageButton4);

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        holder.dots.setOnClickListener(new View.OnClickListener()
        {
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

                if (title.equals("Class Assignments"))
                {
                    final String extracted = assignmentNameArray[position];
                    PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.remove_assignment);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            removeAQTE(extracted, "assignments");
                            return true;
                        }
                    });
                }
                if (title.equals("Class Quizzes"))
                {
                    final String extracted = assignmentNameArray[position];
                    PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.remove_quiz);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            removeAQTE(extracted,"quizzes");
                            return true;
                        }
                    });
                }
                if (title.equals("Class Tests"))
                {
                    final String extracted = assignmentNameArray[position];
                    PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.remove_test);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            removeAQTE(extracted,"tests");
                            return true;
                        }
                    });
                }
                if (title.equals("Class Exams"))
                {
                    final String extracted = assignmentNameArray[position];
                    PopupMenu popupMenu = new PopupMenu(getActivity(), holder.dots);
                    popupMenu.getMenu().add(R.string.remove_exam);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            removeAQTE(extracted,"exams");
                            return true;
                        }
                    });
                }


            }
        });

        String information = null;
        try {
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

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date todaysDate = formatter.parse(today);
            Date assign = formatter.parse(assigned);
            Date dueD = formatter.parse(due);

            if (!title.equals("Class Assignments"))
            {
                if (todaysDate.before(dueD)) {
                    gradientDrawable.setStroke(7, Color.RED);
                    information = assignmentNameArray[position] + "\nScheduled for Future";
                }
                if (todaysDate.after(dueD)) {
                    gradientDrawable.setStroke(7, Color.GREEN);
                    information = assignmentNameArray[position] + "\nCompleted";
                }
                if (todaysDate.equals(dueD)) {
                    gradientDrawable.setStroke(7, Color.YELLOW);
                    information = assignmentNameArray[position] + "\nHappening Today";
                }
            }
            else
            {
                if (todaysDate.before(assign)) {
                    gradientDrawable.setStroke(7, Color.RED);
                    information = assignmentNameArray[position] + "\nScheduled for Future";

                }
                if (todaysDate.after(dueD)) {
                    gradientDrawable.setStroke(7, Color.GREEN);
                    information = assignmentNameArray[position] + "\nCompleted";
                }
                if ((todaysDate.after(assign) && todaysDate.before(dueD)) || todaysDate.equals(assign)) {
                    gradientDrawable.setStroke(7, Color.YELLOW);
                    information = assignmentNameArray[position] + "\nCurrently in Progress";
                }

            }
        }
        catch (ParseException e1)
        {
            e1.printStackTrace();
        }

        SpannableString changeFontSize = new SpannableString(information);
        changeFontSize.setSpan(new RelativeSizeSpan(0.5f), assignmentNameArray[position].length(), information.length(), 0);
        holder.assignmentName.setText(changeFontSize);
        holder.dots.setVisibility(View.VISIBLE);

        return rowView;
    }

    @Override
    public int getCount() {
        return assignmentNameArray.length;
    }

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void removeAQTE(final String name, final String AQTE)
    {

        final String courseName = getActivity().getIntent().getStringExtra("courseName");
        final DatabaseReference currentAQTE = dbAccessfunctions.getChildOfCourses(courseName).child(AQTE);

        new AlertDialog.Builder(context)
                .setTitle("Remove " + removeLastChar(AQTE) +"?")
                .setMessage("Deleting the " + removeLastChar(AQTE) + " will permanently remove it from the database, would you like to proceed?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        currentAQTE.child(name).removeValue();
                        final DatabaseReference classList = dbAccessfunctions.getClassList(courseName);


                        classList.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                dbAccessfunctions.removeAQTEFromStudents(iterator, AQTE,name,classList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

    private String removeLastChar(String str)
    {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 's') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
