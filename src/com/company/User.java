package com.company;

/**
 * Created by Troy on 10/17/16.
 */
public class User {
    Integer id;
    String name;
    String address;
    String email;

    public User(Integer id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
