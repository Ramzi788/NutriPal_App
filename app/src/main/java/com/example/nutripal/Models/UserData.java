package com.example.nutripal.Models;

public class UserData {
    int calorieGoal;

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public UserData(int calorieGoal){
        this.calorieGoal = calorieGoal;
    }
}
