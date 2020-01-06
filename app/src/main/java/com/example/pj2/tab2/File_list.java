package com.example.pj2.tab2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class File_list {

    @SerializedName("filename")// api 리스폰 시 들어올 name이라는 json key
    private String name;
    /*반드시 게터 세터 를 선언해줘야함*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
