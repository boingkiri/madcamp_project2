package com.example.pj2.tab1;

public class Contacts {
//    int id;
    String name;
    String phone;
    String fb_id;

    @Override
    public String toString() {
        return "Contacts{" +
                "name='" + name + '\'' +
                ", phone=" + phone + '\'' +
                ", fb_id='" + fb_id + '\'' +
                '}';
    }

    public Contacts(String name, String phone, String fb_id) {
        this.name = name;
        this.phone = phone;
        this.fb_id = fb_id;
    }

    private String token;

    public String get_fb_Id() {
        return fb_id;
    }

    public void set_fb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
