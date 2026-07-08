package com.sandy.userauthservice.model;

import com.sandy.userauthservice.dtos.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseModel {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserDTO  toUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(this.getUserName());
        userDTO.setEmail(this.getEmail());
        userDTO.setId(this.getId());
        userDTO.setRoles(this.getRoles());
        return userDTO;
    }

    private String userName;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
