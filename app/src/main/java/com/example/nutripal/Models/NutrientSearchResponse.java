package com.example.nutripal.Models;

import java.util.List;

public class NutrientSearchResponse {

    private List<Nutrient> nutrients;

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public Nutrient getCalories() {
        return getSpecificNutrient("Calories");
    }

    public Nutrient getCarbs() {
        return getSpecificNutrient("Carbohydrates");
    }

    public Nutrient getFat() {
        return getSpecificNutrient("Fat");
    }

    public Nutrient getProtein() {
        return getSpecificNutrient("Protein");
    }
    public Nutrient getFiber() {
        return getSpecificNutrient("Fiber");
    }

    private Nutrient getSpecificNutrient(String name) {
        for (Nutrient nutrient : nutrients) {
            if (nutrient.getName().equalsIgnoreCase(name)) {
                return nutrient;
            }
        }
        return null; // Or handle the case where the nutrient is not found
    }
    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }
}