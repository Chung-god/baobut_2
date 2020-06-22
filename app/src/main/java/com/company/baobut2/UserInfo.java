package com.company.baobut2;
import android.app.PendingIntent;

import java.io.Serializable;

public class UserInfo implements Serializable{
    String region;
    Integer[] interest;

    public UserInfo(){

    }

    public UserInfo(String region,Integer[] interest){
        this.region = region;
        this.interest = interest;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer[] getInterest() {
        return interest;
    }

    public void setInterest(Integer[] interest) {
        this.interest = interest;
    }
}
