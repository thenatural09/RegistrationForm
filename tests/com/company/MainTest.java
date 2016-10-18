package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Troy on 10/17/16.
 */
public class MainTest {
    public Connection startConnection () throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTable(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1,"Troy","Where St","tward4@tulane.edu");
        Main.insertUser(conn,user);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 1);
    }

    @Test
    public void testEdit() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1,"Troy","Where St","tward4@tulane.edu");
        Main.insertUser(conn,user);
        user = new User(1,"Bob","There St","thenatural09@live.com");
        Main.editUser(conn,user);
        conn.close();
        assertTrue(user.username.equals("Bob"));
    }

    @Test
    public void testDelete() throws SQLException {
        Connection conn = startConnection();
        User user = new User(1,"Troy","Where St","tward4@tulane.edu");
        Main.insertUser(conn,user);
        Main.deleteUser(conn,1);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 0);
    }
}