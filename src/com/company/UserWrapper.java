package com.company;

import java.util.ArrayList;

/**
 * Created by Troy on 10/17/16.
 */
public class UserWrapper {
    ArrayList<User> users;

    public UserWrapper(ArrayList<User> users) {
        this.users = users;
    }

    public UserWrapper() {
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
