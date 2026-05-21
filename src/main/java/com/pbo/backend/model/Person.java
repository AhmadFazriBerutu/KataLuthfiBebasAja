package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {

    private String nama;
    private String email;
    private String telepon;

    public abstract String getRole();
}