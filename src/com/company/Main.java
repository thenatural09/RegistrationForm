package com.company;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTable(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                (request,response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer serializer = new JsonSerializer();
                    return serializer.deep(true).serialize(users);
                }
        );

        Spark.post(
                "/user",
                (request,response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    User user = parser.parse(body,User.class);
                    insertUser(conn,user);
                    return "";
                }
        );

        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    User user = parser.parse(body,User.class);
                    editUser(conn,user);
                    return "";
                }
        );

        Spark.delete(
                "/user/:id",
                (request,response) -> {
                    JsonParser parser = new JsonParser();
                    Integer id = parser.parse(request.params(":id"));
                    deleteUser(conn,id);
                    return "";
                }
        );

    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY,username VARCHAR,address VARCHAR,email VARCHAR)");
    }

    public static void insertUser (Connection conn,User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (null,?,?,?)");
        stmt.setString(1,user.username);
        stmt.setString(2,user.address);
        stmt.setString(3,user.email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers (Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ArrayList<User> users = new ArrayList<>();
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            User user = new User(id,username,address,email);
            users.add(user);
        }
        return users;
    }

    public static User editUser (Connection conn,User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?,address = ?,email = ? WHERE id = ?");
        stmt.setString(1,user.username);
        stmt.setString(2,user.address);
        stmt.setString(3,user.email);
        stmt.setInt(4,user.id);
        stmt.execute();
        return new User(user.id,user.username,user.address,user.email);
    }

    public static void deleteUser (Connection conn,Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }
}
