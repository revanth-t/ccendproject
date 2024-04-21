package com.ADS;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Base64;

public class ADSAlgorithm {

    public static void main(String[] args) {
        try {
            // Generate a secret key
            SecretKey secretKey = generateSecretKey();

            SecretKey s2 = generateSecretKey2();

            // Message to be encrypted
            String filePath = "/Users/revanththathapudi/Desktop/CC project/ccproj/lorenipsum.txt";

            String originalMessage = readFileContent(filePath);

            // Encrypt the message
            String encryptedMessage = encrypt(originalMessage, secretKey);
            System.out.println("Encrypted Message: " + encryptedMessage);

            String ReencryptMessage = encrypt(encryptedMessage, s2);
            System.out.println("re-encrypted message: " + ReencryptMessage);

            String redecrypted = decrypt(ReencryptMessage, s2);
            System.out.println("re decrypted message: " + redecrypted);

            // Decrypt the message
            String decryptedMessage = decrypt(redecrypted, secretKey);
            System.out.println("Decrypted Message: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate a secret key
    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES key size 128 bits
        return keyGen.generateKey();
    }

    public static SecretKey generateSecretKey2() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES key size 128 bits
        return keyGen.generateKey();
    }

    // Method to encrypt a message
    public static String encrypt(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static byte[] encrypt(byte[] messageBytes, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(messageBytes);
        return encryptedBytes;
    }

    // Method to decrypt a message
    public static String decrypt(String encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }

    // Method to read content from a file
    public static String readFileContent(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

}
