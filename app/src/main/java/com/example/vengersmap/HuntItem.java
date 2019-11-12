package com.example.vengersmap;

public class HuntItem {
    private String huntName;
    private String huntPassword;
    private String huntPark;



    public HuntItem() {}

    public HuntItem(String huntName, String huntPassword, String huntPark){
        this.huntName = huntName;
        this.huntPassword = huntPassword;
        this.huntPark = huntPark;
    }
    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public String getHuntPassword() {
        return huntPassword;
    }

    public void setHuntPassword(String huntPassword) {
        this.huntPassword = huntPassword;
    }
    public String getHuntPark() {
        return huntPark;
    }

    public void setHuntPark(String huntPark) {
        this.huntPark = huntPark;
    }


}
