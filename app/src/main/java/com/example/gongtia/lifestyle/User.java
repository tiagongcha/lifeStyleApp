package com.example.gongtia.lifestyle;

public class User {
    private String id;//key
    private String userName, city, country, sex, goal, lbs, lifeStyle;
    private int age;
    private double height, weight;




    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public void setUid(String uid){
        this.id = uid;
    }

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

    public void setSex(String sex){
        this.sex = sex;
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

    public String getUid(){return this.id;}

    public String getCity(){
        return this.city;
    }

    public String getCountry(){
        return this.country;
    }

    public int getAge(){
        return this.age;
    }

    public String getSex(){
        return this.sex;
    }

    public double getHeight(){
        return this.height;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setGoal(String goal){this.goal = goal;}
    public void setLbs (String lbs) {this.lbs = lbs;}
    public String getGoal(){ return this.goal;}
    public String getLbs() {return this.lbs;}

    public void setLifeStyle(String lifeStyle){this.lifeStyle = lifeStyle;}
    public String getLifeStyle(){return this.lifeStyle;}


}
