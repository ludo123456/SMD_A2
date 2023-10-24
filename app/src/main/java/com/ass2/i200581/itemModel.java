package com.ass2.i200581;

public class itemModel {
    private String name;
    private String price;
    private String location;
    private String date;

    public itemModel(){

    }

    public itemModel(String name, String price, String location, String date) {
        this.name = name;
        this.price = price;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}