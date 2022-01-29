package com.example.jultrautomaintenance;

public class CarModel {

    private String Model,Make,Mileage,Owner,Year;

    public CarModel(String model, String make, String mileage, String year, String owner) {
        Model = model;
        Make = make;
        Mileage = mileage;
        Owner = owner;
        Year = year;
    }
    public CarModel(){}

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getMileage() {
        return Mileage;
    }

    public void setMileage(String mileage) {
        Mileage = mileage;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
