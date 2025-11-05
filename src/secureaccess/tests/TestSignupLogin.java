/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess.tests;

import java.sql.Connection;
import secureaccess.DatabaseConnection;
import secureaccess.UsersDao;

/**
 * 
 * @author linru
 */
public class TestSignupLogin {
    public static void main(String[] args) throws Exception {
        // 1) Open your SQLite DB file
        Connection conn = DatabaseConnection.getConnection();

        UsersDao dao = new UsersDao(conn);

        // 2) Sign up a user
        int id = dao.createUser("Doei", "Doei@example.com", "MyStrongPassword!".toCharArray());
        System.out.println("Created user id = " + id);

        // 3) Try login(true)
        boolean ok1 = dao.verifyLogin("lDoei", "MyStrongPassword!".toCharArray());
        System.out.println("Login correct password: " + ok1);

        // 4) Wrong password (false)
        boolean ok2 = dao.verifyLogin("Doei", "wrongpass".toCharArray());
        System.out.println("Login wrong password: " + ok2);

        conn.close();
    }
}
