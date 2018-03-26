package com.example.ammar.teacherandmev10.StudentDrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.Activities.MainActivity;
import com.example.ammar.teacherandmev10.Activities.RoleSelection;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ammar on 2018-02-03.
 */

public class ExpandableAQTECustomAdapter extends BaseExpandableListAdapter
{

    private Context _context;
    // header titles

    private List<String> _listDataHeader;
    // child data in format of header title, child title

    private HashMap<String, List<String>> _listDataChild;

    //don't remove conView
    private View conView;

    private Activity activity;
    private DatabaseReference dbRef;
    private String AQTE;
    private FragmentManager fm;

    public ExpandableAQTECustomAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, Activity activity,
                                       DatabaseReference db, String AQTE, FragmentManager frgMan)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.activity = activity;
        this.dbRef = db;
        this.AQTE = AQTE;
        this.fm = frgMan;
    }

    public class Holder
    {
        TextView AQTEName;
        ImageView dots;
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent)
    {
        conView = convertView;
        final Holder holder = new Holder();
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.student_assignment_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

//        ImageView threeDots = (ImageView) convertView.findViewById(R.id.threeDotsStudentView);
//        threeDots.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));

        holder.AQTEName = lblListHeader;
        holder.dots = (ImageView) convertView.findViewById(R.id.threeDotsStudentView);
        holder.dots.setVisibility(View.GONE);
        if (isExpanded)
        {
            holder.dots.setVisibility(View.VISIBLE);
        }
        holder.dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(_context, holder.dots);
                popupMenu.getMenu().add("Edit?");
                popupMenu.show();


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        boolean isAssignment = false;
                        if (AQTE.equals("assignments"))
                        {
                            isAssignment = true;
                        }
                        showPopup(groupPosition, isAssignment);
                        return true;
                    }
                });
            }
        });
        return convertView;
    }
//TODO figure how to make fields editable fml
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.student_assignment_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    public void showPopup(final int groupPosition, final boolean isAssignment)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_context);
        LayoutInflater inflater =  activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        TextView handedInTitle = (TextView) dialogView.findViewById(R.id.handedInTitle);
        spinner.setVisibility(View.GONE);
        handedInTitle.setVisibility(View.GONE);
        if (isAssignment)
        {
            spinner.setVisibility(View.VISIBLE);
            handedInTitle.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_context,
                R.array.trueOrFalse, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialogBuilder.setTitle("Edit items");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                final String aqteName = (String) getGroup(groupPosition);
                boolean check = true;
                TextView gradeEdit = (TextView) dialogView.findViewById(R.id.gradeEdit);
                final String grade = gradeEdit.getText().toString().trim();
                final String handIn;
                if (!android.text.TextUtils.isDigitsOnly(grade) || grade.length() == 0)
                {
                    check = false;
                    Toast.makeText(_context,"Invalid grade",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    double dGrade = Double.parseDouble(grade);
                    if (dGrade < 0 || dGrade > 100)
                    {
                        Toast.makeText(_context,"Invalid grade",Toast.LENGTH_SHORT).show();
                        check = false;
                    }
                }
                if (isAssignment && check)
                {
                    handIn = spinner.getSelectedItem().toString();
                    dbRef.child(aqteName).child("Grade").setValue(grade);
                    dbRef.child(aqteName).child("submitted").setValue(handIn);
                }
                else if (check)
                {
                    dbRef.child(aqteName).child("Grade").setValue(grade);
                }

                Fragment currentFragment = null;
                if (AQTE.equals("assignments"))
                {
                    currentFragment = fm.findFragmentByTag("sAssignments");

                }

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
