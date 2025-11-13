/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author linru
 */
public final class CryptoUtil {
    private CryptoUtil() {}

    private static final int AES_KEY_BITS   = 256;  //32 bytes
    private static final int GCM_TAG_BITS   = 128;  //16 bytes tag
    private static final int GCM_IV_BYTES   = 12;   //12-byte nonce for GCM
    private static final int SALT_BYTES     = 16;
    private static final int ITERATIONS     = 100_000;
    
    public static final class EncryptionResult {
        public final String ciphertextBase64;
        public final String saltBase64;
        public final String ivBase64;
        public EncryptionResult(String ct, String salt, String iv) {
            this.ciphertextBase64 = ct; this.saltBase64 = salt; this.ivBase64 = iv;
        }
    }

    //encrypt a plaintext using master password
    public static EncryptionResult encryptPassword(char[] masterPassword, String plaintext) {
        byte[] iv = new byte[GCM_IV_BYTES];
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(iv);
        new SecureRandom().nextBytes(salt);

        try {
            SecretKeySpec key = deriveAesKey(masterPassword, salt);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcm = new GCMParameterSpec(GCM_TAG_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcm);
            byte[] ct = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return new EncryptionResult(
                Base64.getEncoder().encodeToString(ct),
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(iv)
            );
        } catch (Exception e) {
            throw new IllegalStateException("AES-GCM encrypt failed", e);
        } finally {
            
        }
    }

    //decrypt
    public static String decryptPassword(char[] masterPassword, String saltBase64, String ivBase64, String ciphertextBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            byte[] iv = Base64.getDecoder().decode(ivBase64);
            byte[] ct = Base64.getDecoder().decode(ciphertextBase64);

            SecretKeySpec key = deriveAesKey(masterPassword, salt);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcm = new GCMParameterSpec(GCM_TAG_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcm);
            byte[] pt = cipher.doFinal(ct);
            return new String(pt, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("AES-GCM decrypt failed", e);
        }
    }

    private static SecretKeySpec deriveAesKey(char[] masterPassword, byte[] salt) throws Exception {
        try {
            PBEKeySpec spec = new PBEKeySpec(masterPassword, salt, ITERATIONS, AES_KEY_BITS);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] keyBytes = f.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } finally {

        }
    }
    
    //fixed 12/11/2025 Linru
    //Generates a strong random password for the GUI
    public static String generateStrongPassword(int length) {
        final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SYMBOLS = "!@#$%^&*()-_+=<>?";
        final String ALL = UPPER + LOWER + DIGITS + SYMBOLS;

        SecureRandom rnd = new SecureRandom();
        StringBuilder pwd = new StringBuilder(length);

        if (length >= 4) {
            pwd.append(UPPER.charAt(rnd.nextInt(UPPER.length())));
            pwd.append(LOWER.charAt(rnd.nextInt(LOWER.length())));
            pwd.append(DIGITS.charAt(rnd.nextInt(DIGITS.length())));
            pwd.append(SYMBOLS.charAt(rnd.nextInt(SYMBOLS.length())));
        }
        for (int i = pwd.length(); i < length; i++) {
            pwd.append(ALL.charAt(rnd.nextInt(ALL.length())));
        }
        for (int i = 0; i < pwd.length(); i++) {
            int j = rnd.nextInt(pwd.length());
            char t = pwd.charAt(i);
            pwd.setCharAt(i, pwd.charAt(j));
            pwd.setCharAt(j, t);
        }
        return pwd.toString();
    }

    //Generate a random 256-bit AES key
    public static byte[] generateKey() {
        byte[] key = new byte[32]; // 256-bit
        new java.security.SecureRandom().nextBytes(key);
        return key;
    }

    //AES-GCM encrypt with provided raw key
    public static String encrypt(String plain, byte[] rawKey) throws Exception {
        byte[] iv = new byte[12];
        new java.security.SecureRandom().nextBytes(iv);
        javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        javax.crypto.spec.SecretKeySpec k = new javax.crypto.spec.SecretKeySpec(rawKey, "AES");
        c.init(javax.crypto.Cipher.ENCRYPT_MODE, k, new javax.crypto.spec.GCMParameterSpec(128, iv));
        byte[] ct = c.doFinal(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        byte[] blob = new byte[iv.length + ct.length];
        System.arraycopy(iv, 0, blob, 0, iv.length);
        System.arraycopy(ct, 0, blob, iv.length, ct.length);
        return java.util.Base64.getEncoder().encodeToString(blob);
    }

    //AES-GCM decrypt the Base64
    public static String decrypt(String base64Blob, byte[] rawKey) throws Exception {
        byte[] blob = java.util.Base64.getDecoder().decode(base64Blob);
        byte[] iv = java.util.Arrays.copyOfRange(blob, 0, 12);
        byte[] ct = java.util.Arrays.copyOfRange(blob, 12, blob.length);
        javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        javax.crypto.spec.SecretKeySpec k = new javax.crypto.spec.SecretKeySpec(rawKey, "AES");
        c.init(javax.crypto.Cipher.DECRYPT_MODE, k, new javax.crypto.spec.GCMParameterSpec(128, iv));
        return new String(c.doFinal(ct), java.nio.charset.StandardCharsets.UTF_8);
    }
}
