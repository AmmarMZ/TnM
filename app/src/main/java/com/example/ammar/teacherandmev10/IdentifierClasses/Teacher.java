package com.example.ammar.teacherandmev10.IdentifierClasses;

import java.util.ArrayList;

/**
 * Created by Ammar on 2017-05-05.
 */

//teacher is one of the user types
    //Hierarchy: Teacher - Course - Student
    //Teachers create courses, in a course they add students
    //Whenever a teacher makes a new course they have to add a #new student object type
    //Students can be copied and moved from one class to another but their grades,assignments etc are nullified

public class Teacher {

    private ArrayList<Course> courses;
    private String uniqueID;


    public Teacher() {

        this.courses = new ArrayList<>();
        this.uniqueID = "empty";
    }

    public Teacher(String uID)
    {
        this.uniqueID = uID;
    }


    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }


}
