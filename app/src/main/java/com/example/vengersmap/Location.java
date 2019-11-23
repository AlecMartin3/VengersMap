package com.example.vengersmap;

/**
 * Location class with a name, Lat and Long coords.
 */
public class Location {
    private double x;
    private double y;
    private String name;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
