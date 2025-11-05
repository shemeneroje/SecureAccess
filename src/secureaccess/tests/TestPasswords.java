/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess.tests;

import java.sql.Connection;
import java.util.List;
import secureaccess.DatabaseConnection;
import secureaccess.PasswordsDao;

/**
 *
 * @author linru
 */
public class TestPasswords {
    public static void main(String[]args) throws Exception{
        Connection conn = DatabaseConnection.getConnection();
        
        PasswordsDao pw = new PasswordsDao(conn);
        
        //assume already created a user (id)
        int userId = 6;
        char[] master = "MtStrongPassword!".toCharArray();
        
        //add a couple of entrise
        pw.addPassword(userId, master,
                "Instagram", "Loaging@instagram.com", "https://instagram.com",
                "MailPass!123", "main email account");

        pw.addPassword(userId, master,
                "Netflix", "Queicc", "https://www.netflix.com",
                "QN890tflix!!", "family account");

        System.out.println("Inserted new password entries!");


        //list
        List<PasswordsDao.PasswordRow> rows = pw.listPasswords(userId);
        System.out.println("\n Stored passwords:");
        for (var r : rows) {
            System.out.println(r.id + " | " + r.serviceName + " | " + r.serviceUsername + " | " + r.url + " | " + r.createdAt);
        }

        //reveal one
         if (!rows.isEmpty()) {
            int firstId = rows.get(0).id;
            String revealed = pw.revealPassword(firstId, master);
            System.out.println("\nDecrypted password for " + rows.get(0).serviceName + ": " + revealed);
        }
        
        // hygiene
        java.util.Arrays.fill(master, '\0'); // clear from memory
        conn.close();
        System.out.println("\n Done!");
       
    }
}
