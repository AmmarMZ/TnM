package com.example.ammar.teacherandmev10.IdentifierClasses;

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

}
