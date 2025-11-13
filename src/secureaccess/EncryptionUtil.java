/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author vvtat
 */

/**
 * This acts as the main interface for the application's encyption and decryption operations. It handles loading the AES key
 * via KeyManager and delegates cryptographic operations to CryptoUtils.
 */
public class EncryptionUtil {
    private static byte[] AES_KEY = null;

    // a Static initializer to ensure the key is loaded or created once
    // when the class is first accessed.
    static {
        try {
            AES_KEY = KeyManager.loadOrCreateKey();
            if (AES_KEY == null || AES_KEY.length != 32) {
                throw new IllegalStateException("Invalid AES key length");
            }
            System.out.println("[EncryptionUtil] AES key ready, len=" + AES_KEY.length);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "FATAL: Encryption key is missing or invalid.\n" + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    /**
     * Encrypts a plaintext password using the application's master AES key.
     * The result is a Base64-encoded string containing the IV and ciphertext.
     * * @param plainText The password to encrypt.
     * returns The Base64-encoded encrypted string, or null on failure.
     */
    public static String encrypt(String plainText) {
        if (AES_KEY == null) return null;
        try {
            return CryptoUtil.encrypt(plainText, AES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypts a Base64-encoded encrypted password blob using the application's 
     * master AES key.
     * * @param base64Blob The Base64-encoded string containing IV and ciphertext.
     * returns The decrypted plaintext password, or null on failure.
     */
    public static String decrypt(String base64Blob) {
        if (AES_KEY == null) return null;
        if (base64Blob == null || base64Blob.isEmpty()) return "";
        try {
            return CryptoUtil.decrypt(base64Blob, AES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "[Decryption Failed]";
        }
    }
    
    // Method to securely hash the master passwords for user login authentication
    // relies on the  Hashing.java for authentication
//    public static String hashMasterPassword(char[] password) {
//        return Hashing.hashPassword(password);
//    }
    
    //Public static getter method to safely retrieve the key
    /**
     * Retrieves the application's master AES key.
     * returns The AES key byte array, or null if initialization failed.
     */
    public static byte[] getAESKey() {
        return AES_KEY;
    }
    
    //Securely wipes the AES key from memory. Will be called on application exit/logout.
    public static void wipeKey() {
        if (AES_KEY != null) 
            // Overwrite the array contents with zeros to securely wipe it from memory
            java.util.Arrays.fill(AES_KEY, (byte) 0);
            AES_KEY = null;
        
    }
    
}
