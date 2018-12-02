package com.Sec;


import javax.crypto.*;


import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import java.util.Arrays;

public class CryptoUtils {

    private static int n = 0;

    private static void doCrypto(int cipherModel, String password, File inputFile, File outputFile) {
        try {

            KeyGenerator keyGenerator = new KeyGenerator();
            byte[] key = keyGenerator.generateKey(password, 16);
            System.out.println("Key :::::::" + Arrays.toString(key));
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(cipherModel, keySpec);
            System.out.println("cipher init done");

//            FileInputStream inputStream = new FileInputStream(inputFile);
//            //byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
//            byte[] inputBytes = new byte[(int)inputFile.length()];
//            //System.out.println(Arrays.toString(inputBytes));
//            inputStream.read(inputBytes);
//            System.out.println("inputStream reading done");
//            //System.out.println(inputStream.getChannel());
//
//
//            byte[] outputBytes = cipher.doFinal(inputBytes);
//            System.out.println("Encrypting/decrypting done");
//            FileOutputStream outputStream = new FileOutputStream(outputFile);
//            outputStream.write(outputBytes);
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] inputBytes = new byte[7];
            byte[] outputBytes;
            int count;
            while ((count = inputStream.read(inputBytes))>0){
                outputBytes = cipher.doFinal(inputBytes);
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();


        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }


    }

    public static void encrypt(String password, File inputFile, Path directoryOutput) {
        File[] listFiles;
        Path directoryOutput1 = directoryOutput;
        if (!inputFile.isDirectory()) {
            File outputFile = new File(directoryOutput + "/" + inputFile.getName() + ".encrypt");
            doCrypto(Cipher.ENCRYPT_MODE, password, inputFile, outputFile);
            inputFile.delete();
        } else {
            if (CryptoUtils.n == 0) {
                n = n + 1;
            } else {
                directoryOutput1 = Paths.get(directoryOutput + "/" + inputFile.getName());
                File diretory = new File(String.valueOf(directoryOutput1));
                diretory.mkdir();
            }
            System.out.println("Name inputFile: " + inputFile.getName());
            listFiles = inputFile.listFiles();
            assert listFiles != null;
            for (File file : listFiles) {
                encrypt(password, file, directoryOutput1);
            }
            inputFile.delete();

        }


    }

    public static void decrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }


    /*public static void main(String[] args) throws CryptoException {
        String key ="tydufhcbfgsirugh";
        String fileName = "/home/buixuanthai/Desktop/plaintext";
        File inputFile = new File(fileName);
        File outputBinaryFile = new File(fileName+".encrypt");
        File outputPlainFile = new File(fileName+".decrypt");
        try {
            doCrypto(Cipher.ENCRYPT_MODE,key,inputFile,outputBinaryFile);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        doCrypto(Cipher.DECRYPT_MODE,key,outputBinaryFile,outputPlainFile);


    }*/

}
//    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(key.getBytes());
//                    byte[] keyBytes = new byte[16];
//                    System.arraycopy(messageDigest.digest(), 0, keyBytes, 0, keyBytes.length);
//                    System.out.println("keyBytes: " + Arrays.toString(keyBytes));
//                    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
//                    //Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
//                    System.out.println("secretKey be maked");
//                    System.out.println(secretKey);