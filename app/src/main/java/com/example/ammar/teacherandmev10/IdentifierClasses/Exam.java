package com.example.ammar.teacherandmev10.IdentifierClasses;

/**
 * Created by Ammar on 2017-05-05.
 */

//Exam class for school exams
    //Hiearchy: Teacher - Course - Student - Exam

public class Exam {

    private String name;
    private double grade;
    private double weight;

    public Exam() {
        this.name = "Unassigned";
        this.grade = 0;
        this.weight = 0;
    }  //default constructor

    public Exam(String name, double grade, double weight) {
        this.name = name;
        this.grade = grade;
        this.weight = weight;
    } //parameterized constructor

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
} //end of class
