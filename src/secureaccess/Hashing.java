/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Linru
 */

public class Hashing {
    private Hashing() {}

    public static final int ITERATIONS = 100_000;
    private static final int SALT_BYTES = 16;   // 128-bit
    private static final int KEY_BITS   = 256;  // 256-bit

    public static String generateSaltBase64() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password with Base64-encoded salt; returns Base64-encoded hash
    public static String hashPassword(char[] password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_BITS);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = f.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            System.out.println("PBKDF2 failed: " + e.getMessage());
            return null;
        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }

    // Constant-time verify
    public static boolean verify(char[] candidate, String saltBase64, String expectedHashBase64) {
        String cand = hashPassword(candidate, saltBase64);
        if (cand == null) return false;
        byte[] a = Base64.getDecoder().decode(cand);
        byte[] b = Base64.getDecoder().decode(expectedHashBase64);
        if (a.length != b.length) return false;
        int r = 0;
        for (int i = 0; i < a.length; i++) r |= a[i] ^ b[i];
        return r == 0;
    }
}

