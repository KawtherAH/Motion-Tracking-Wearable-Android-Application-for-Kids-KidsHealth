package com.example.kidshealthv3;

public class User {
    public String username, email, childName, age, weight, height, BMI;

    public User(){

    }
    public User(String username, String email, String childName, String age, String weight, String height, String BMI) {
        this.username = username;
        this.email = email;
        this.childName = childName;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.BMI = BMI;
    }
}
