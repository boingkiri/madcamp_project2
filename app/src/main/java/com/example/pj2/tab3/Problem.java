package com.example.pj2.tab3;

import android.provider.ContactsContract;

public class Problem {

    private String building;
    private String floor;
    private String problem;

//    public Problem(String building, String floor, String problem){
//        this.building = building;
//        this.floor = floor;
//        this.problem = problem;
//    }

    public String getBuilding() {
        return this.building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getfloor() {
        return this.floor;
    }

    public void setfloor(String floor) {
        this.floor = floor;
    }

    public String getproblem() {
        return this.problem;
    }

    public void setproblem(String problem) {
        this.problem = problem;
    }

}
