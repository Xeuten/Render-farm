package com.example.renderFarmServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

    public User(){}

    public User(Long userId, String password) {
        this.user_id = userId;
        this.password = password;
        this.is_logged_in = false;
    }

    @Id
    @Column
    private Long user_id;

    @Column
    private String password;

    @Column
    private Boolean is_logged_in;

    public Long getUser_id() { return user_id; }

    public void setUser_id(Long user_id) { this.user_id = user_id; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Boolean getIs_logged_in() { return is_logged_in; }

    public void setIs_logged_in(Boolean is_logged_in) { this.is_logged_in = is_logged_in; }
}
