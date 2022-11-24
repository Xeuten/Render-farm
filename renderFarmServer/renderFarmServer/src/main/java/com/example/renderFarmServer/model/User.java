package com.example.renderFarmServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

    public User(){}

    public User(String userId) { this.username = userId; }

    @Id
    @Column
    private String username;

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

}
