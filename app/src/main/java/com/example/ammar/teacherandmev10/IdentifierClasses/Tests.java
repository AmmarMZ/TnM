package com.example.ammar.teacherandmev10.IdentifierClasses;

/**
 * Created by Ammar on 2017-05-05.
 */

//class for School Tests
    //Hierarchy: Teacher - Course - Student - Tests

public class Tests {

    private String name;
    private double grade;
    private double weight;

    public Tests() {
        this.name = "Unassigned";
        this.grade = 0;
        this.weight = 0;
    }

    public Tests(String name, Course course, Teacher teacher, double grade, double weight) {
        this.name = name;
        this.grade = grade;
        this.weight = weight;
    }

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
}
