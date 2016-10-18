package com.company;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTable(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                (request,response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer serializer = new JsonSerializer();
                    UserWrapper wrapper = new UserWrapper(users);
                    return serializer.deep(true).serialize(wrapper);
                }
        );

        Spark.post(
                "/user",
                (request,response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    HashMap<String,String> m = parser.parse(body);
                    User user = new User(m.get("name"),m.get("address"),m.get("email"));
                    insertUser(conn,user.name,user.address,user.email);
                    return null;
                }
        );

        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    HashMap<String,String> m = parser.parse(body);
                    User user = new User(m.get("name"),m.get("address"),m.get("email"));
                    editUser(conn,user.name,user.address,user.email);
                    return null;
                }
        );

        Spark.delete(
                "/user:id",
                (request,response) -> {
                    Integer id = Integer.valueOf(request.queryParams(":id"));
                    deleteUser(conn,id);
                    return null;
                }
        );

    }

    public static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY,name VARCHAR,address VARCHAR,email VARCHAR)");
    }

    public static void insertUser (Connection conn,String name,String address,String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (null,?,?,?)");
        stmt.setString(1,name);
        stmt.setString(2,address);
        stmt.setString(3,email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers (Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            Integer id = results.getInt("id");
            String name = results.getString("name");
            String address = results.getString("address");
            String email = results.getString("email");
            User user = new User(id,name,address,email);
            users.add(user);
        }
        return users;
    }

    public static User editUser (Connection conn,String name,String address,String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?,address = ?,email = ?");
        stmt.setString(1,name);
        stmt.setString(2,address);
        stmt.setString(3,email);
        stmt.execute();
        return new User(name,address,email);
    }

    public static void deleteUser (Connection conn,Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }
}
