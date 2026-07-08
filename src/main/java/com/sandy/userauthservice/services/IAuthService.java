package com.sandy.userauthservice.services;

import com.sandy.userauthservice.model.User;

public interface IAuthService {
    User signUp(String name, String email, String password);
    User login(String email, String password);
}
