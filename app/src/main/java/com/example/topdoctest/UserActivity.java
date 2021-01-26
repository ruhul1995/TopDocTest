package com.example.topdoctest;

public class UserActivity {

    String userName, loginTime;

    public UserActivity(String userName, String loginTime) {
        this.userName = userName;
        this.loginTime = loginTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
