/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import secureaccess.loginGUI;

public class loginTest {

    
    public void testEmptyFields() {
        loginGUI login = new loginGUI();
        login.setVisible(false);

        // making empty inputs
        login.getEmailJtf().setText("");
        login.getPasswordJpf().setText("");

        // user clicks the submit button
        login.getSubmitJbtn().doClick();

        // printing the values
        System.out.println("Email: '" + login.getEmailJtf().getText() + "'");
        // new converts the password from char[] to string so that I can test it
        System.out.println("Password: '" + new String(login.getPasswordJpf().getPassword()) + "'");
        System.out.println("Expects: login failed due to the user failing to enter their details");
    }

    
    public void testIncorrectLogin() {
        loginGUI login = new loginGUI();
        login.setVisible(false);

        // user enters wrong login info
        login.getEmailJtf().setText("wrong@example.com");
        login.getPasswordJpf().setText("123456");

        login.getSubmitJbtn().doClick();     

        System.out.println("Email: '" + login.getEmailJtf().getText() + "'");
        System.out.println("Password: '" + new String(login.getPasswordJpf().getPassword()) + "'");
        System.out.println("Expectes: login failed due to an incorrect username or password");
    }

    
    public void testCorrectLogin() {
        loginGUI login = new loginGUI();
        login.setVisible(false);

        // user enters correct login info
        login.getEmailJtf().setText("test@example.com");
        login.getPasswordJpf().setText("correct_password");

        login.getSubmitJbtn().doClick();

        System.out.println("Email: '" + login.getEmailJtf().getText() + "'");
        System.out.println("Password: '" + new String(login.getPasswordJpf().getPassword()) + "'");
        System.out.println("Expects: login successful user entered their correct details");
    }
}
