/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

/**
 *
 * @author vvtat
 */
public class PasswordEntry {
    private int id;
    private String userEmail; // The email of the user who owns this password entry
    private String name; // Name of the service/website (e.g., "Google", "Twitter")
    private String username; // The login username or email for that service
    private String encryptedPassword; // The encrypted password string
    private String url;
    private String category;
    private String notes;

    //with all parameters to view for all passwords page
    public PasswordEntry(int id, String userEmail, String name, String username, String encryptedPassword, String url, String category, String notes) {
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
        this.category = category;
        this.notes = notes;
    }
    
    // Constructor for adding new entry (ID is set by the DB)
    public PasswordEntry(String userEmail, String name, String username, String encryptedPassword, String url, String category, String notes) {
        // ID to be set by the database
        this.userEmail = userEmail;
        this.name = name;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
        this.category = category;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    //To display on the dashboard list
    @Override
    public String toString() {
        return  name + "\n" + username;
    }

    
    
    
}
