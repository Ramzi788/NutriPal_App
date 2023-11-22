package com.example.nutripal.Models;

import java.util.List;

public class NutrientSearchResponse {
    private List<Nutrient> nutrients;

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }
}