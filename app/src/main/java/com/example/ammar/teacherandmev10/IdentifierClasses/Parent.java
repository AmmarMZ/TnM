package com.example.ammar.teacherandmev10.IdentifierClasses;

/**
 * Created by Ammar on 2017-05-05.
 */

//The parent class connects to Teachers
    // Hierarchy: Parent -> Teachers - Course - Student

public class Parent {


    private Student child;
    private String uniqueID;


    public Parent(String uID) {
        this.uniqueID = uID;
    }

    public Parent()
    {

    }//default constructor



    public Student getChild() {
        return child;
    }

    public void setChild(Student child) {
        this.child = child;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}//end of class
