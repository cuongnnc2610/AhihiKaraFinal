package com.example.cuong.discover_ahihikara.model;

import java.io.Serializable;

public class Account implements Serializable {

    private String userName;
    private String password;

    public Account() {
        this.userName = "";
        this.password = "";
    }

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
