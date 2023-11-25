package com.example.nutripal.Models;
public class Meal {
    String name;
    int calories;
    int carbs;
    int fat;
    int protein;
    int fiber;
    String mealType;

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public Meal(String name, int calories, int carbs, int fat, int protein, int fiber) {
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.fiber = fiber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }
}