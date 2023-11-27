package com.example.nutripal.Models;

public class StepData {
    String date;
    int steps;

    public StepData(String date, int steps) {
        this.date = date;
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
