package com.example.ammar.teacherandmev10.IdentifierClasses;

import java.util.Date;

/**
 * Created by Ammar on 2017-05-05.
 */

//class Assignments represents school assignments
//should be self explanatory until I read it 3 months later and try to fix something....
//Hierarchy: Teacher - Course - Students - Assignments


public class Assignments {

    private String assignedDate; //yyyy/MM/dd
    private String dueDate;
    private String name;
    private double weight;

    public Assignments() {
        this.assignedDate = null;
        this.dueDate = null;
        this.name = "Unassigned";
        this.weight = 0;
    }

    public Assignments(String assignedDate, String dueDate, String name,double grade, double weight) {
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.name = name;
        this.weight = weight;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
} //end of class
