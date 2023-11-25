package com.example.nutripal.Models;

public class UserData {
    int calorieGoal, height, weight;
    String firstName, lastName;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public UserData(int calorieGoal, int height, int weight, String firstName, String lastName) {
        this.calorieGoal = calorieGoal;
        this.height = height;
        this.weight = weight;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }


}
