package com.sandy.userauthservice.services;

import com.sandy.userauthservice.dtos.UserToken;
import com.sandy.userauthservice.model.User;

public interface IAuthService {
    User signUp(String name, String email, String password);
    UserToken login(String email, String password);
    Boolean validateToken(String token);
}
