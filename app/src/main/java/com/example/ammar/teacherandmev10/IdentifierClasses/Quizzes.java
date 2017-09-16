package com.example.ammar.teacherandmev10.IdentifierClasses;

/**
 * Created by Ammar on 2017-05-05.
 */

//Class for school quizzes
    //Hierarchy: Teacher-Course-Student-Quizzes

public class Quizzes {

 //   private String name;
    private String assignedDate; //yyyy/MM/dd - the date the quiz is created
    private String dueDate;
    private double weight;


    public Quizzes() {
       // this.name = "Unassigned";
        this.weight = 0;
    }//end of default constructor

    public Quizzes(String name, double weight) {
       // this.name = name;
        this.weight = weight;
    }// end of parameterized constructor

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
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
