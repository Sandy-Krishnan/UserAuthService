package com.sandy.userauthservice.services;

import com.sandy.userauthservice.exception.IncorrectPasswordException;
import com.sandy.userauthservice.exception.UserAlreadyNotExistException;
import com.sandy.userauthservice.exception.UserNotRegisteredException;
import com.sandy.userauthservice.model.Role;
import com.sandy.userauthservice.model.State;
import com.sandy.userauthservice.model.User;
import com.sandy.userauthservice.repositories.RoleRepository;
import com.sandy.userauthservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User signUp(String name, String email, String password) {
        //uniqueness
        Role role = null;
        Optional<User> optionalUser  = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            throw new UserAlreadyNotExistException("User with this email " + email + " already exist");
        }
        //create user
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setPassword(password);
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdateAt(System.currentTimeMillis());
        user.setState(State.ACTIVE);

        //assign roles

        Optional<Role> optionalRole = roleRepository.findByValue("DEFAULT");

        if(optionalRole.isEmpty()) {
            role = new Role();
            role.setValue("DEFAULT");
            role.setCreatedAt(System.currentTimeMillis());
            role.setUpdateAt(System.currentTimeMillis());
            role.setState(State.ACTIVE);
            roleRepository.save(role);
        } else {
            role = optionalRole.get();
        }

        List<Role> roles = List.of(role);

        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty() ) {
            throw new UserNotRegisteredException("Uer with email " + email + " does not exist");
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(password)) {
            throw new IncorrectPasswordException("Invalid credentials");
        }

        return user;
    }
}
