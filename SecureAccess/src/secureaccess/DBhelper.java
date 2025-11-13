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
 * @author vvtat
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
    //Lin edited
    public static void initializeUsersTable() {
        final String sql =
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id INTEGER PRIMARY KEY," +
            "  username TEXT NOT NULL UNIQUE COLLATE NOCASE," +
            "  email TEXT UNIQUE COLLATE NOCASE," +
            "  password_hash TEXT NOT NULL COLLATE BINARY," +
            "  password_salt TEXT," +
            "  two_factor_enabled INTEGER," +
            "  two_factor_secret TEXT," +
            "  account_locked INTEGER," +
            "  last_login TEXT," +
            "  created_at TEXT," +
            "  updated_at TEXT" +
            ");";
        
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
    
   /* public static boolean saveUser(String name, String email, String hashedPass) {
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
    
    //Retrieves user ID by email (Needed for linking passwords by ID).
    public static int getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user ID: " + e.getMessage());
        }
        return -1; // Indicates user not found or error
    } */
    // Save user with the correct columns
    
    //Lin edited
    public static boolean saveUser(String username, String email, String passwordHash, String passwordSalt) {
        final String sql =
            "INSERT INTO users (username, email, password_hash, password_salt, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, datetime('now'), datetime('now'))";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setString(4, passwordSalt);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    public static int getUserIdByEmail(String email) {
        final String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user ID: " + e.getMessage());
        }
        return -1;
    }


    // Read the hashed password (and salt if you need it)
    public static String getHashedPasswordByEmail(String email) {
        final String sql = "SELECT password_hash FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("password_hash");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    public static String getSaltByEmail(String email) {
        final String sql = "SELECT password_salt FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("password_salt");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving salt: " + e.getMessage());
        }
        return null;
    }

    
    //Creates the passwords table if it doesn't exist.
    public static void initializePasswordsTable() {
        final String sqlPragma = "PRAGMA foreign_keys = ON;"; // ensure FKs enforced in SQLite
        final String sql =
            "CREATE TABLE IF NOT EXISTS passwords (" +
            "  password_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  user_id      INTEGER NOT NULL," +
            "  service_name TEXT    NOT NULL," +
            "  service_username TEXT," +
            "  url          TEXT    NOT NULL," +
            "  notes        TEXT," +
            "  enc_password TEXT    NOT NULL," +
            "  enc_salt     TEXT    NOT NULL," +
            "  enc_iv       TEXT    NOT NULL," +
            "  created_at   TEXT    NOT NULL DEFAULT (CURRENT_TIMESTAMP)," +
            "  updated_at   TEXT    NOT NULL DEFAULT (CURRENT_TIMESTAMP)," +
            // FK should reference users(id) (the PK). If your DB really has users(user_id), change below back.
            "  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ");";
        try (Connection conn = DatabaseConnection.connect(); 
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Passwords table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating passwords table: " + e.getMessage());
        }
    }
    
    // Insert a password row (convert email -> user_id first)
    public static boolean savePassword(PasswordEntry entry) {
        int userId = getUserIdByEmail(entry.getUserEmail());
        if (userId < 0) return false;

        final String sql =
            "INSERT INTO passwords (user_id, service_name, service_username, url, notes, enc_password, enc_salt, enc_iv, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, entry.getName());        // service_name
            ps.setString(3, entry.getUsername());    // service_username
            ps.setString(4, entry.getUrl());
            ps.setString(5, entry.getNotes());
            ps.setString(6, entry.getEncryptedPassword()); // enc_password
            ps.setString(7, entry.getSalt());               // enc_salt
            ps.setString(8, entry.getIv());                 // enc_iv
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving password: " + e.getMessage());
            return false;
        }
    }

    public static List<PasswordEntry> getAllPasswords(String ownerEmail) {
        List<PasswordEntry> list = new ArrayList<>();
        int userId = getUserIdByEmail(ownerEmail);
        if (userId < 0) return list;

        final String sql =
            "SELECT password_id, service_name, service_username, url, notes, enc_password, enc_salt, enc_iv " +
            "FROM passwords WHERE user_id = ? ORDER BY password_id DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PasswordEntry e = new PasswordEntry(
                        rs.getInt("password_id"),
                        ownerEmail,
                        rs.getString("service_name"),
                        rs.getString("service_username"),
                        rs.getString("enc_password"),
                        rs.getString("url"),
                        /* category */ null,             // see note below
                        rs.getString("notes")
                    );
                    // if your PasswordEntry includes salt/iv, set them too
                    e.setSalt(rs.getString("enc_salt"));
                    e.setIv(rs.getString("enc_iv"));
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving passwords: " + e.getMessage());
        }
        return list;
    }

    public static boolean updatePassword(PasswordEntry entry) {
        int userId = getUserIdByEmail(entry.getUserEmail());
        if (userId < 0) return false;

        final String sql =
            "UPDATE passwords SET service_name=?, service_username=?, url=?, notes=?, " +
            "enc_password=?, enc_salt=?, enc_iv=?, updated_at=datetime('now') " +
            "WHERE password_id=? AND user_id=?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entry.getName());
            ps.setString(2, entry.getUsername());
            ps.setString(3, entry.getUrl());
            ps.setString(4, entry.getNotes());
            ps.setString(5, entry.getEncryptedPassword());
            ps.setString(6, entry.getSalt());
            ps.setString(7, entry.getIv());
            ps.setInt(8, entry.getId());   // password_id
            ps.setInt(9, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePassword(int passwordId, String ownerEmail) {
        int userId = getUserIdByEmail(ownerEmail);
        if (userId < 0) return false;

        final String sql = "DELETE FROM passwords WHERE password_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, passwordId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting password: " + e.getMessage());
            return false;
        }
    }

   /* //Saves a new password entry for a specific user.
    public static boolean savePassword(PasswordEntry entry) {
        String sql = "INSERT INTO passwords(user_id, name, username, password, url, category, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
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
        String sql = "SELECT id, name, username, password, url, category, notes FROM passwords WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userEmail);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Construct a PasswordEntry object from the database row
                PasswordEntry entry = new PasswordEntry(
                    rs.getInt("id"),
                    userEmail, // We know the email, it's the one we queried
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password"), // The encrypted password
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
        String sql = "UPDATE passwords SET name = ?, username = ?, password = ?, url = ?, category = ?, notes = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getName());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getEncryptedPassword());
            pstmt.setString(4, entry.getUrl());
            pstmt.setString(5, entry.getCategory());
            pstmt.setString(6, entry.getNotes());
            pstmt.setInt(7, entry.getId());
            //pstmt.setString(8, entry.getUserEmail()); // Important for security/ownership check
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    //Deletes a password entry by its ID.
    public static boolean deletePassword(int id, String userEmail) {
        //Check against 'user_id'
        String sql = "DELETE FROM passwords WHERE id = ? AND user_id = ?";

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
    }*/

    // main method for testing database connection and table creation
    public static void main(String[] args) {
        DatabaseConnection.connect();
        initializeUsersTable();
    }
}

