package com.example.spoontest.Models;
import java.util.List;

public class RecipeSearchResponse {
    private List<Recipe> results;
    private int offset;
    private int number;
    private int totalResults;

    // Constructor, getters and setters

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }


}

