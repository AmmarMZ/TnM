package com.example.ammar.teacherandmev10.TeacherDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Ammar on 2017-06-11.
 */

//TODO when deleting AQTE the resepctive AQTE must also be removed from all students in the class

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
        TextView assignmentDesc;
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
                            removeAssignment(extracted);
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
                            removeQuiz(extracted);
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
                            removeTest(extracted);
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
                            removeExam(extracted);
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


        SpannableString changeFontSize =  new SpannableString(information);
        changeFontSize.setSpan(new RelativeSizeSpan(0.5f),assignmentNameArray[position].length(),information.length(),0);

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

    public void removeAssignment(final String assignment)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_assignment)
                .setMessage(R.string.delete_assignment)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                    DatabaseReference db = (DatabaseReference) objReceived;

                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.getParent().child("assignments"). child(assignment).removeValue();
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

    public void removeQuiz(final String quiz)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_quiz)
                .setMessage(R.string.delete_quiz)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                    DatabaseReference db = (DatabaseReference) objReceived;

                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.getParent().child("quizzes"). child(quiz).removeValue();
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

    public void removeTest(final String test)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_test)
                .setMessage(R.string.delete_test)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                    DatabaseReference db = (DatabaseReference) objReceived;

                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.getParent().child("tests"). child(test).removeValue();
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

    public void removeExam(final String exam)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_exam)
                .setMessage(R.string.delete_exam)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    Object objReceived = ((ObjectWrapperForBinder)getActivity().getIntent().getExtras().getBinder("classList")).getData();
                    DatabaseReference db = (DatabaseReference) objReceived;

                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.getParent().child("exams"). child(exam).removeValue();
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