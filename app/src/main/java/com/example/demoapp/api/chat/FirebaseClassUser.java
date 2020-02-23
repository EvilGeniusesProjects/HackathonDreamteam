package com.example.demoapp.api.chat;

public class FirebaseClassUser {
    public String email;
    public String password;
    public String name;
    public String lastname;

    public String role;
    public String course;
    public String faculty;
    public String specialty;

    public FirebaseClassUser() {
    }

    public FirebaseClassUser(String email, String password, String name, String lastname, String role, String course, String faculty, String specialty) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        this.course = course;
        this.faculty = faculty;
        this.specialty = specialty;
    }
}
