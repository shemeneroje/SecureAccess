/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
/**
 *
 * @author shaun
 */

public class DBhelper {
    private static final String URL = "jdbc:sqlite:password_manager.db";

    // Connect to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            //trying to connect to the database
            conn = (Connection) DriverManager.getConnection(URL);  //getconnection gives an object that lets code interact with the database
            System.out.println("Connection successful!");
        } catch (SQLException e) {  
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    // create users table
    public static void initializeUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" 
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT," //autoincrement makes sure every input has a unique ID
                   + "name TEXT NOT NULL,"  //name cant be empty
                   + "email TEXT NOT NULL UNIQUE," //email must be unique to the user
                   + "password TEXT NOT NULL" //hashed password cant be empty
                   + ");";
        //try catch automatically closes database connection once the try block ends, even if there is an error
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // saving users
    public static boolean saveUser(String name, String email, String hashedPass) {
        //the ??? are placeholders for where the name, email, and password are going
        String sql = "INSERT INTO users(name,email,password) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); //name is inserted where the first ? is
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPass);
            pstmt.executeUpdate();  
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    // main method for testing database connection and table creation
    public static void main(String[] args) {
        connect();
        initializeUsersTable();
    }
}

