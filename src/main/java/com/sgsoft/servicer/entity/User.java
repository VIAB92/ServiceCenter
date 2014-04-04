package com.sgsoft.servicer.entity;

import java.util.List;

/**
 * Created by Viktor Rotar on 03.04.14.
 */
public class User {
    private Integer id;
    private String login;
    private String password;
    private String fullname;
    private String email;
    private Role role;
    private List<State> statesVisibleList;

    public List<State> getStatesVisibleList() {
        return statesVisibleList;
    }

    public void setStatesVisibleList(List<State> statesVisibleList) {
        this.statesVisibleList = statesVisibleList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
