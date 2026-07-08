package com.sandy.userauthservice.model;

import jakarta.persistence.Entity;

@Entity
public class Role extends BaseModel{
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
