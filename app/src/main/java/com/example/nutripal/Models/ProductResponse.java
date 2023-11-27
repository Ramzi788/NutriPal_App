package com.example.nutripal.Models;

import java.util.List;

public class ProductResponse {
    int id;
    private String title;
    private Nutrition nutrition;
    private List<GroceryItem> results;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public List<GroceryItem> getResults() {
        return results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class Nutrition {
        private List<Nutrient> nutrients;
        private String calories;
        private String protein;
        private String fat;
        private String carbs;
        private String fiber;

        // Getters and Setters
        public List<Nutrient> getNutrients() {
            return nutrients;
        }

        public void setNutrients(List<Nutrient> nutrients) {
            this.nutrients = nutrients;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getProtein() {
            return protein;
        }

        public void setProtein(String protein) {
            this.protein = protein;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getCarbs() {
            return carbs;
        }

        public void setCarbs(String carbs) {
            this.carbs = carbs;
        }

        public String getFiber() {
            return fiber;
        }

        public void setFiber(String fiber) {
            this.fiber = fiber;
        }

        @Override
        public String toString() {
            return "Nutrition{" +
                    "nutrients=" + nutrients +
                    ", calories='" + calories + '\'' +
                    ", protein='" + protein + '\'' +
                    ", fat='" + fat + '\'' +
                    ", carbs='" + carbs + '\'' +
                    ", fiber='" + fiber + '\'' +
                    '}';
        }
    }
}
