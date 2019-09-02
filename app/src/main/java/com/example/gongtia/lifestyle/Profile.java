package com.example.gongtia.lifestyle;

public class Profile {
    private String userName, city, country;
    private int age;
    private double height, weight;
    private boolean isMale;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setSex(boolean isMale){
        this.isMale = isMale;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getCity(){
        return this.city;
    }

    public String getCountry(){
        return this.country;
    }

    public int getAge(){
        return this.age;
    }

    public boolean getSex(){
        return this.isMale;
    }

    public double getHeight(){
        return this.height;
    }

    public double getWeight() {
        return this.weight;
    }
}
