package com.example.nutripal.Models;

import java.util.ArrayList;

public class GroceryItem {
    public int id;
    public String title;
    public ArrayList<String> badges;
    public ArrayList<String> importantBadges;
    public ArrayList<String> breadcrumbs;
    public String generatedText;
    public String imageType;
    public Object ingredientCount;
    public String ingredientList;
    public int likes;
    public ProductResponse.Nutrition nutrition;
    public double price;
    public double spoonacularScore;
    public String credits;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<String> badges) {
        this.badges = badges;
    }

    public ArrayList<String> getImportantBadges() {
        return importantBadges;
    }

    public void setImportantBadges(ArrayList<String> importantBadges) {
        this.importantBadges = importantBadges;
    }

    public ArrayList<String> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(ArrayList<String> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Object getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(Object ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public String getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(String ingredientList) {
        this.ingredientList = ingredientList;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ProductResponse.Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(ProductResponse.Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSpoonacularScore() {
        return spoonacularScore;
    }

    public void setSpoonacularScore(double spoonacularScore) {
        this.spoonacularScore = spoonacularScore;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}
