package com.sandy.userauthservice.dtos;

import com.sandy.userauthservice.model.User;

public class UserToken {

    public UserToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String token;
    private User user;
}
