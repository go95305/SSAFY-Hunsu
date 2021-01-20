package com.project.hunsu.dto;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String gender;
    private String size;
    private double height;

    @NotNull
    @Column(unique = true)
    private String nickName;

}
