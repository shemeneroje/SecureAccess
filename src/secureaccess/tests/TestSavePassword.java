/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess.tests;

import java.sql.Connection;

import java.sql.Connection;
import secureaccess.DatabaseConnection;
import secureaccess.PasswordsDao;

/**
 *
 * @author linru
 */
public class TestSavePassword {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        PasswordsDao dao = new PasswordsDao(conn);

        dao.savePassword(3, "MyStrongPassword!".toCharArray(),
                 "Gmail", "https://mail.google.com", "MailPass!123");

        conn.close();
    }
}