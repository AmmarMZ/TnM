package com.example.ammar.teacherandmev10.IdentifierClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.ammar.teacherandmev10.R;
import com.google.firebase.database.DatabaseReference;

import junit.framework.Test;

import java.util.ArrayList;

/**
 * Created by Ammar on 2017-05-05.
 */

//class for School Courses
    //Hierarchy: Teacher-Course

public class Course {

    private ArrayList<Student> classList;
    private double courseAverage;
    private ArrayList<Assignments> assignments;
    private ArrayList<Quizzes> quizzes;
    private ArrayList<Tests> tests;
    private ArrayList<Exam> exams;


    public Course() {
        this.classList = new ArrayList<>();
        this.assignments= new ArrayList<>();
        this.quizzes = new ArrayList<>();
        this. tests = new ArrayList<>();
        this.exams = new ArrayList<>();
        this.courseAverage = 0;
    }

    public void addAssignment(Assignments assignment)
    {
        this.assignments.add(assignment);
    }

    public Course(ArrayList<Student> classList, double courseAverage) {
        this.classList = classList;
        this.courseAverage = courseAverage;
    }

    public ArrayList<Student> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<Student> classList) {
        this.classList = classList;
    }

    public double getCourseAverage() {
        return courseAverage;
    }

    public void setCourseAverage(double courseAverage) {
        this.courseAverage = courseAverage;
    }

    public void removeCourse(final String course, final DatabaseReference db, Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_course)
                .setMessage(R.string.delete_course)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.child(course).removeValue();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

}
