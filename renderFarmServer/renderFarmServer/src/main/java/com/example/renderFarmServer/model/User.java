package com.example.renderFarmServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

    public User(){}

    public User(Long userId) {
        this.user_id = userId;
    }

    @Id
    @Column
    private Long user_id;

    public Long getUser_id() { return user_id; }

    public void setUser_id(Long user_id) { this.user_id = user_id; }

}
