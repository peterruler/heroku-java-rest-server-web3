package com.example.bean;

public class Animal {
    private int id = 1;
    private String name = "";
    private String image = "";
    private double size = 1;
    private int weight = 1;
    private int age = 1;
    private int offspring = 1;
    private int speed = 1;

    public Animal() {

    }

    public Animal(int id, String name, String image, double size, int weight, int age, int offspring, int speed) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.size = size;
        this.weight = weight;
        this.age = age;
        this.offspring = offspring;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getOffspring() {
        return offspring;
    }

    public void setOffspring(int offspring) {
        this.offspring = offspring;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
