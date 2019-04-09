package com.apps.harsh.foodcalorietracker.model;

public class InfoModel {
    String name;
    String date;
    String time;
    String fat;
    String proteins;
    String calories;
    String carbohydrates;

    public InfoModel() {
    }

    public InfoModel(String name, String date, String time, String fat, String proteins, String calories, String carbohydrates) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.fat = fat;
        this.proteins = proteins;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
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

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
