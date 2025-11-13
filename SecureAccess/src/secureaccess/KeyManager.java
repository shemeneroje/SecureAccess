/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package secureaccess;

import java.nio.file.*;
/**
 *
 * @author shemeneroje
 */

//Linru fixed the code 12/11/2025
public class KeyManager {
    // Save under user home, e.g. C:\Users\<you>\.secureaccess\aes.key
    private static final Path KEY_PATH = Paths.get(System.getProperty("user.home"),
            ".secureaccess", "aes.key");

    public static byte[] loadOrCreateKey() throws Exception {
        try {
            //ensure directory exists
            Files.createDirectories(KEY_PATH.getParent());

            if (Files.exists(KEY_PATH)) {
                byte[] key = Files.readAllBytes(KEY_PATH);
                if (key.length == 32) {
                    System.out.println("[KeyManager] Loaded AES key from: " + KEY_PATH);
                    return key;
                }
                System.err.println("[KeyManager] Invalid key length (" + key.length + "). Regenerating.");
            }

            //create a fresh 256-bit key
            byte[] key = CryptoUtil.generateKey();
            Files.write(KEY_PATH, key, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            System.out.println("[KeyManager] Created new AES key at: " + KEY_PATH);
            return key;
        } catch (Exception e) {
            System.err.println("[KeyManager] Failed to load/create key at: " + KEY_PATH);
            throw e;
        }
    }
}