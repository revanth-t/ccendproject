// package com;

// import java.io.*;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// import javax.crypto.SecretKey;

// public class FileChunker {

//     public static void main(String[] args) throws Exception {
//         String filePath = "/Users/revanththathapudi/Desktop/CC project/ccproj/lorenipsum.txt"; // Replace this with the
//                                                                                                // actual file path
//         int numChunks = 4;

//         try {
//             // Read the file
//             byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
//             String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
//             // SymmetricEncryptionDemo sed = new SymmetricEncryptionDemo();

//             SecretKey secretKey = SymmetricEncryptionDemo.generateSecretKey();

//             String encryptedMasterFile = SymmetricEncryptionDemo.encrypt(fileContent, secretKey);

//             long chunkSize = (long) Math.ceil((double) fileBytes.length / numChunks);

//             // Create chunks and write them to files
//             for (int i = 0; i < numChunks; i++) {
//                 int startIndex = (int) (i * chunkSize);
//                 int endIndex = (int) Math.min((i + 1) * chunkSize, fileBytes.length);
//                 byte[] chunkBytes = new byte[endIndex - startIndex];

//                 System.arraycopy(fileBytes, startIndex, chunkBytes, 0, chunkBytes.length);

//                 String ChunkContent = new String(chunkBytes, StandardCharsets.UTF_8);

//                 SecretKey ReEncryptionKey = SymmetricEncryptionDemo.generateSecretKey2();

//                 String ReEncryptedChunk = SymmetricEncryptionDemo.encrypt(ChunkContent, ReEncryptionKey);
//                 String Decrypted_ReEncChunk = SymmetricEncryptionDemo.decrypt(ReEncryptedChunk, ReEncryptionKey);

//                 System.out.println("Chunk Content is " + (i + 1) + " " + ChunkContent);
//                 System.out.println("Chunk " + (i + 1) + "is Re encrypted with " + ReEncryptionKey + "\n");
//                 System.out.println("Re Encrypted Chunk " + (i + 1) + " " + ReEncryptedChunk + "\n");
//                 System.out.println(
//                         "Decrypted Re-Encrypted Chunk " + (i + 1) + " " + Decrypted_ReEncChunk + "jkshdf" + "\n");

//                 // Write chunk to file
//                 String chunkFilePath = filePath + ".part" + (i + 1);
//                 Path chunkPath = Paths.get(chunkFilePath);
//                 Files.write(chunkPath, chunkBytes);
//                 System.out.println("Chunk " + (i + 1) + " saved to: " + chunkFilePath);
//             }

//             System.out.println("File divided into " + numChunks + " chunks successfully.");
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
package com.ADS;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class FileProcessor {
    public static void main(String[] args) throws Exception {
        String filePath = "/Users/revanththathapudi/Desktop/CC project/ccproj/100mb-examplefile-com.txt"; // Replace
                                                                                                          // this with
                                                                                                          // the
        // actual file path

        // "/Users/revanththathapudi/Desktop/CC project/ccproj/lorenipsum.txt"
        String MergedFilePath = "/Users/revanththathapudi/Desktop/CC project/ccproj/Merged";
        int numChunks = 4;

        List<String> chunkFilePaths = new ArrayList<>();

        try {
            byte[] fileBytes = readFile(filePath);
            String fileContent = convertBytesToString(fileBytes);

            SecretKey MastersecretKey = ADSAlgorithm.generateSecretKey();
            String encryptedMasterFile = ADSAlgorithm.encrypt(fileContent, MastersecretKey);

            byte[] encryptedBytes = encryptedMasterFile.getBytes(StandardCharsets.UTF_8);

            chunkFilePaths = SplitChunks(encryptedBytes, filePath, numChunks);

            mergeChunks(chunkFilePaths, MergedFilePath);

            byte[] MergedBytes = readFile(MergedFilePath);
            String MergedContent = convertBytesToString(MergedBytes);
            String DecryptedMasterFile = ADSAlgorithm.decrypt(MergedContent, MastersecretKey);

            // System.out.println("Successfully Decrypted File is " + DecryptedMasterFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public static String convertBytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static List<String> SplitChunks(byte[] fileBytes, String filePath, int numChunks) throws Exception {
        long chunkSize = (long) Math.ceil((double) fileBytes.length / numChunks);
        List<String> paths = new ArrayList<>();

        for (int i = 0; i < numChunks; i++) {
            int startIndex = (int) (i * chunkSize);
            int endIndex = (int) Math.min((i + 1) * chunkSize, fileBytes.length);
            byte[] chunkBytes = new byte[endIndex - startIndex];

            System.arraycopy(fileBytes, startIndex, chunkBytes, 0, chunkBytes.length);

            String chunkContent = convertBytesToString(chunkBytes);

            SecretKey reEncryptionKey = ADSAlgorithm.generateSecretKey2();
            long startTime = System.currentTimeMillis();
            String reEncryptedChunk = ADSAlgorithm.encrypt(chunkContent, reEncryptionKey);
            long endTime = System.currentTimeMillis();
            String decryptedReEncChunk = ADSAlgorithm.decrypt(reEncryptedChunk, reEncryptionKey);

            System.out.println("Chunk Content is " + (i + 1) + " " + chunkContent);
            System.out.println("Chunk " + (i + 1) + " is Re encrypted with " + reEncryptionKey + "\n");
            // System.out.println("Re Encrypted Chunk " + (i + 1) + " " + reEncryptedChunk +
            // "\n");
            System.out.println("Encryption speed is " + (endTime - startTime) + " ms");
            // System.out.println("Decrypted Re-Encrypted Chunk " + (i + 1) + " " +
            // decryptedReEncChunk + "\n");

            String chunkFilePath = filePath + ".part" + (i + 1);
            Path chunkPath = Paths.get(chunkFilePath);
            Files.write(chunkPath, chunkBytes);

            paths.add(chunkFilePath);
            System.out.println("Chunk " + (i + 1) + " saved to: " + chunkFilePath);
        }

        System.out.println("File divided into " + numChunks + " chunks successfully.");
        return paths;
    }

    public static void mergeChunks(List<String> chunkFilePaths, String mergedFilePath) throws IOException {
        StringBuilder mergedContent = new StringBuilder();

        for (String chunkFilePath : chunkFilePaths) {
            byte[] chunkBytes = Files.readAllBytes(Paths.get(chunkFilePath));
            String chunkContent = new String(chunkBytes, StandardCharsets.UTF_8);
            mergedContent.append(chunkContent);
        }

        // Write the merged content to the output file
        Files.write(Paths.get(mergedFilePath), mergedContent.toString().getBytes(StandardCharsets.UTF_8));
    }

}
