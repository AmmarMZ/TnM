package com.example.ammar.teacherandmev10.IdentifierClasses;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ammar on 2017-05-05.
 */

//Class for users that login
    //users are verified using google firebase authentication


public class User {

    private String phoneNumber;
    private Teacher teacher;
    private Parent parent;
    private FirebaseUser userAccount;
    private String firstName;
    private String lastName;

    public User() {
        this.phoneNumber = "empty";
        this.teacher = new Teacher();
        this.parent = new Parent();
        this.userAccount = FirebaseAuth.getInstance().getCurrentUser();
        this.firstName = "empty";
        this.lastName = "empty";
    }

    public String setUniqueID(String emailAddress) {
        String temp = emailAddress.replace(".","DOT");
        String temp2 = temp.replace("@","AT");

        return temp2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public FirebaseUser getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(FirebaseUser userAccount) {
        this.userAccount = userAccount;
    }
}
