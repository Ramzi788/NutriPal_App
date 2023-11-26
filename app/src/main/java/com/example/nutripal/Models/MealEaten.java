package com.example.nutripal.Models;

public class MealEaten {
    private int dateMonth;
    private String name;
    private String date;  // Assuming the date is in String format
    private int carbs;
    private int calories;
    private int fiber;
    private int dateDay;
    private int protein;
    private String mealType;
    private int dateYear;
    private int fat;

    // Getters and Setters

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    // You might also want to override toString() for easy debugging
    @Override
    public String toString() {
        return "MealEaten{" +
                "dateMonth=" + dateMonth +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", carbs=" + carbs +
                ", calories=" + calories +
                ", fiber=" + fiber +
                ", dateDay=" + dateDay +
                ", protein=" + protein +
                ", mealType='" + mealType + '\'' +
                ", dateYear=" + dateYear +
                ", fat=" + fat +
                '}';
    }
}

