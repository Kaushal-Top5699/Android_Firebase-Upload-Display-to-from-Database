package com.example.this_is_kaushal.profile2;

import android.widget.ImageView;

/**
 * Created by this_is_kaushal on 7/12/2017.
 */

public class UserInformation {

    private String name;
    private String email;
    private String phone_num;

    public UserInformation(String name, String email, String phone_num) {
        this.name = name;
        this.email = email;
        this.phone_num = phone_num;
    }
    public UserInformation() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone_num='" + phone_num + '\'' +
                '}';
    }
}
