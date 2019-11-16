package com.example.vengersmap;

public class Player {

    String email;
    String artName;

    Player(){}

    Player(String email, String artName){
        this.email = email;
        this.artName = artName;
    }
    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
