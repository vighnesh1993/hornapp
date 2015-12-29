package com.horn.workshop;

/**
 * Created by vighnu on 12/6/2015.
 */
public class User {
    String name, username, password, imageurl;
    int age;

    public User(String name,String username, String password){
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public User(String username,String password){
        this.username = username;
        this.password = password;
        this.age = -1;
        this.name = "";
    }
}
