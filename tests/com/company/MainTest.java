package com.company;

import org.junit.Test;
import org.w3c.dom.ranges.DocumentRange;

import java.sql.Connection;
import java.sql.Driver;
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
        Main.insertUser(conn,"Troy","Where st","tward4@tulane.edu");
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 1);
    }

    @Test
    public void testEdit() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn,"Troy","Where st","tward4@tulane.edu");
        User user = Main.editUser(conn,"Bob","There St","thenatural09@live.com");
        conn.close();
        assertTrue(user.name.equals("Bob"));
    }

    @Test
    public void testDelete() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn,"Troy","Where st","tward4@tulane.edu");
        Main.deleteUser(conn,1);
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 0);
    }
}