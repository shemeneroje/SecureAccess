/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
/**
 *
 * @author shaun
 */

public class DBhelper {
    private static final String URL = "jdbc:sqlite:password_manager.db";

    // Connect to the database (//Removing this and using the connection in the DatabaseConnection File.) @Virginiah
//    public static Connection connect() {
//        Connection conn = null;
//        try {
//            //trying to connect to the database
//            conn = (Connection) DriverManager.getConnection(URL);  //getconnection gives an object that lets code interact with the database
//            System.out.println("Connection successful!");
//        } catch (SQLException e) {  
//            System.out.println("Connection failed: " + e.getMessage());
//        }
//        return conn;
//    }

    
    // create users table
//    public static void initializeUsersTable() {
//        String sql = "CREATE TABLE IF NOT EXISTS users (" 
//                   + "id INTEGER PRIMARY KEY AUTOINCREMENT," //autoincrement makes sure every input has a unique ID
//                   + "name TEXT NOT NULL,"  //name cant be empty
//                   + "email TEXT NOT NULL UNIQUE," //email must be unique to the user
//                   + "password TEXT NOT NULL" //hashed password cant be empty
//                   + ");";
//        //try catch automatically closes database connection once the try block ends, even if there is an error
//        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
//            stmt.execute(sql);
//            System.out.println("Users table created or already exists.");
//        } catch (SQLException e) {
//            System.out.println("Error creating table: " + e.getMessage());
//        }
//    }
    
    public static void initializeUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" 
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT," //autoincrement makes sure every input has a unique ID
                   + "name TEXT NOT NULL,"  //name cant be empty
                   + "email TEXT NOT NULL UNIQUE," //email must be unique to the user
                   + "password TEXT NOT NULL" //hashed password cant be empty
                   + ");";
        
        // Corrected: Uses DatabaseConnection.connect() to get the connection @Virginiah
        // try-with-resources ensures the connection and statement are closed automatically.
        try (Connection conn = DatabaseConnection.connect(); 
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Users table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // saving users
//    public static boolean saveUser(String name, String email, String hashedPass) {
//        //the ??? are placeholders for where the name, email, and password are going
//        String sql = "INSERT INTO users(name,email,password) VALUES (?, ?, ?)";
//        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, name); //name is inserted where the first ? is
//            pstmt.setString(2, email);
//            pstmt.setString(3, hashedPass);
//            pstmt.executeUpdate();  
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Error saving user: " + e.getMessage());
//            return false;
//        }
//    }
    
    public static boolean saveUser(String name, String email, String hashedPass) {
        // SQL query with placeholders for security
        String sql = "INSERT INTO users(name,email,password) VALUES (?, ?, ?)";
        
        // Corrected: Uses DatabaseConnection.connect() to get the connection  @Virginiah
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPass);
            pstmt.executeUpdate();  
            return true;
        } catch (SQLException e) {
            // Print error, particularly for UNIQUE constraint violation on email
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    
    
    //@Author: Virginiah
    //Retrieves the hashed password for a given email address.
    public static String getHashedPasswordByEmail(String email) {
        String sql = "SELECT password FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }
    
    //Creates the passwords table if it doesn't exist.
    public static void initializePasswordsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS passwords (" 
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "user_id INTEGER NOT NULL,"
                   + "name TEXT NOT NULL,"
                   + "username TEXT NOT NULL,"
                   + "password TEXT NOT NULL," // This should be encrypted!
                   + "url TEXT,"
                   + "category TEXT,"
                   + "notes TEXT,"
                   + "FOREIGN KEY(user_id) REFERENCES users(id)"
                   + ");";
        try (Connection conn = DatabaseConnection.connect(); 
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Passwords table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating passwords table: " + e.getMessage());
        }
    }

    //Saves a new password entry for a specific user.
    public static boolean savePassword(PasswordEntry entry) {
        String sql = "INSERT INTO passwords(user_email, service_name, username, encrypted_password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getUserEmail());
            pstmt.setString(2, entry.getName()); // name maps to service_name
            pstmt.setString(3, entry.getUsername()); // username maps to service_username
            pstmt.setString(4, entry.getEncryptedPassword());
            pstmt.setString(5, entry.getUrl());
            pstmt.setString(6, entry.getCategory());
            pstmt.setString(7, entry.getNotes());
            
            pstmt.executeUpdate();  
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving password: " + e.getMessage());
            return false;
        }
    }

    // Retrieves all password entries for a given user ID.
    public static List<PasswordEntry> getAllPasswords(String userEmail) {
        List<PasswordEntry> entries = new ArrayList<>();
        String sql = "SELECT id, service_name, username, encrypted_password FROM passwords WHERE user_email = ?";
        
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userEmail);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Construct a PasswordEntry object from the database row
                PasswordEntry entry = new PasswordEntry(
                    rs.getInt("id"),
                    userEmail, // We know the email, it's the one we queried
                    rs.getString("service_name"),
                    rs.getString("service_username"),
                    rs.getString("encrypted_password"),
                    rs.getString("url"),
                    rs.getString("category"),
                    rs.getString("notes")
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving passwords: " + e.getMessage());
        }
        return entries;
    }

    //Updates an existing password entry.
    public static boolean updatePassword(PasswordEntry entry) {
        String sql = "UPDATE passwords SET service_name = ?, username = ?, encrypted_password = ? WHERE id = ? AND user_email = ?";
        
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getName());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getEncryptedPassword());
            pstmt.setString(4, entry.getUrl());
            pstmt.setString(5, entry.getCategory());
            pstmt.setString(6, entry.getNotes());
            pstmt.setInt(7, entry.getId());
            pstmt.setString(8, entry.getUserEmail()); // Important for security/ownership check
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    //Deletes a password entry by its ID.
    public static boolean deletePassword(int id, String userEmail) {
        String sql = "DELETE FROM passwords WHERE id = ? AND user_email = ?";

        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, userEmail); // Security check: ensure only the owner can delete
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting password: " + e.getMessage());
            return false;
        }
    }

    // main method for testing database connection and table creation
    public static void main(String[] args) {
        DatabaseConnection.connect();
        initializeUsersTable();
    }
}

