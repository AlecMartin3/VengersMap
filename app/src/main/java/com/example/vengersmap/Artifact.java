package com.example.vengersmap;

import java.io.Serializable;

/**
 * The Artifact has a name, Lat and Log coords.
 */
public class Artifact implements Serializable {
    private String artName;
    private double x;
    private double y;

    Artifact(){}

    Artifact(String artName){
        this.artName = artName;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

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
}
