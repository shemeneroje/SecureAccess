/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import secureaccess.signupGUI;


public class signupGUITest {

    
    public void testEmptyFields() {
        signupGUI signup = new signupGUI();
        signup.setVisible(false);

        // making empty inputs
        signup.getNameJtf().setText("");
        signup.getPasswordJpf().setText("");
        signup.getCpasswordJpf().setText("");
        signup.getEmailJtf().setText("");

        //https://stackoverflow.com/questions/1355254/jbutton-doclick-clicks-the-buttons-but-does-not-perform-the-function
        // user clicks the submit button
        signup.getSubmitJbtn().doClick();

        // printing the values
        System.out.println("Name: '" + signup.getNameJtf().getText() + "'");
        //new converts the passwords from char[] to string so that i can test it
        System.out.println("Password: '" + new String(signup.getPasswordJpf().getPassword()) + "'");
        System.out.println("Confirm Password: '" + new String(signup.getCpasswordJpf().getPassword()) + "'");
        System.out.println("Email: '" + signup.getEmailJtf().getText() + "'");
    }

    
    public void testSuccessfulSignup() {
        signupGUI signup = new signupGUI();
        signup.setVisible(false);

        // create user inputs
        signup.getNameJtf().setText("John");
        signup.getPasswordJpf().setText("pass123");
        signup.getCpasswordJpf().setText("pass123");
        signup.getEmailJtf().setText("john@example.com");

        
        signup.getSubmitJbtn().doClick();

        // checks if the verify panel is visible or not
        System.out.println("Verify panel visible? " + signup.getVerifyPanel().isVisible());
    }

    
    public void testOTPVerification() {
        signupGUI signup = new signupGUI();
        signup.setVisible(false);

        // creating an otp
        signup.getEmailJtf1().setText("john@example.com");
        signup.getSendOTPjbtn().doClick();

        // user enters correct otp
        signup.getOtpJtf().setText(signup.getGeneratedOTP());
        signup.getVerifyJbtn().doClick();
        System.out.println("OTP verified correctly? " + !signup.getVerifyPanel().isVisible());

        // enter the wrong otp
        signup.getVerifyPanel().setVisible(true); // show the verify panel again
        signup.getOtpJtf().setText("000000");
        signup.getVerifyJbtn().doClick();
        System.out.println("OTP verified incorrectly? " + !signup.getVerifyPanel().isVisible());
    }
}
