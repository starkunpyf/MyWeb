package com.webserver.entity;

import java.io.Serializable;

public class User implements Serializable {
    static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String nickname;
    private int age;

    public User(String username, String password, String nickname, int age) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
