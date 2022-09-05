package com.example.miniproject.AccountManagement.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    public String userID;
    public String studentID;
    public String fullname;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Student(String userID, String studentID, String fullname) {
        this.userID = userID;
        this.studentID = studentID;
        this.fullname = fullname;
    }
}