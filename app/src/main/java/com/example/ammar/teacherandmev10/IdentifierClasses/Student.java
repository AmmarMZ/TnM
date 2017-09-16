package com.example.ammar.teacherandmev10.IdentifierClasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ammar on 2017-05-05.
 */

// the student class
    //Students are independent of each course
    //A new student object needs to be created everytime a teacher creates a course

public class Student {

    public enum Behaviour{Green,Yellow,Red};
    public enum attendanceStatus{Present,Absent,Sick,Other};
    private String firstName;
    private String lastName;
    private Parent parent;
    private String uniqueID;    //This ID is how the Parent will access the student through the teacher via the database

    private HashMap<String,Behaviour> behaviourMap;
    private ArrayList<Quizzes> quizzes;
    private ArrayList<Tests> tests;
   //   private ArrayList<Object> assignments;
    private ArrayList<Exam> exams;
    private HashMap<String,String> attendance;

    private ArrayList<String> notes;

    public Student()
    {}


    public Student(String fName, String lName)
    {
        this.firstName = fName;
        this.lastName = lName;
        quizzes = new ArrayList<>();
        tests = new ArrayList<>();
     //   assignments = new ArrayList<>();
        exams = new ArrayList<>();
        attendance = new HashMap<>();
    }// default constructor

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public HashMap<String, Behaviour> getBehaviourMap() {
        return behaviourMap;
    }

    public void setBehaviourMap(HashMap<String, Behaviour> behaviourMap) {
        this.behaviourMap = behaviourMap;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArrayList<Quizzes> getQuizzes() {
        return quizzes;
    }

    public ArrayList<Tests> getTests() {
        return tests;
    }

//    public ArrayList<Object> getAssignments() {
//        return assignments;
//    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public HashMap<String, String> getAttendance() {
        return attendance;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setQuizzes(ArrayList<Quizzes> quizzes) {
        this.quizzes = quizzes;
    }

    public void setTests(ArrayList<Tests> tests) {
        this.tests = tests;
    }

//    public void setAssignments(ArrayList<Object> assignments) {
//        this.assignments = assignments;
//    }

    public void setExams(ArrayList<Exam> exams) {
        this.exams = exams;
    }

    public void setAttendance(HashMap<String, String> attendance) {
        this.attendance = attendance;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}
