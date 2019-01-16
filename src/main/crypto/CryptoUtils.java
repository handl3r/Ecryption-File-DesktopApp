package main.crypto;


import javax.crypto.*;


import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import java.util.Arrays;

public class CryptoUtils {

    private static int n = 0;

    private void doCrypto(int cipherModel, String password, File inputFile, File outputFile) {
        try {

            KeyGenerator keyGenerator = new KeyGenerator();
            byte[] key = keyGenerator.generateKey(password, 16);
            System.out.println("Key :::::::" + Arrays.toString(key));
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(cipherModel, keySpec, ivSpec);
            System.out.println("cipher init done");


            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] inputBytes = new byte[64];

            int count;
            while ((count = inputStream.read(inputBytes)) > 0) {
                //outputBytes = cipher.doFinal(inputBytes);
                //outputStream.write(outputBytes);
                //
                byte[] output = cipher.update(inputBytes, 0, count);
                if (output != null)
                    outputStream.write(output);
                //

            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null)
                outputStream.write(outputBytes);
            inputStream.close();
            outputStream.close();


        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }

    public void crypto(String password, File inputFile, Path directoryOutput, int mode) {
        File[] listFiles;
        Path directoryOutput1 = directoryOutput;
        if (!inputFile.isDirectory()) {
            if (mode == 1) {
                File outputFile = new File(directoryOutput + "/" + inputFile.getName() + ".encrypt");

                doCrypto(Cipher.ENCRYPT_MODE, password, inputFile, outputFile);
                inputFile.delete();
            } else if (mode == 2) {
                String fileName = inputFile.getName().replaceAll(".encrypt", "");
                File outputFile = new File(directoryOutput + "/" + fileName);
                System.out.println("File no .ecnrypt : " + outputFile);
                doCrypto(Cipher.DECRYPT_MODE, password, inputFile, outputFile);
                inputFile.delete();

            }
        } else {

            directoryOutput1 = Paths.get(directoryOutput + "/" + inputFile.getName());
            File directory = new File(String.valueOf(directoryOutput1));
            directory.mkdir();

            System.out.println("Name inputFile: " + inputFile.getName());
            listFiles = inputFile.listFiles();
            assert listFiles != null;
            for (File file : listFiles) {
                crypto(password, file, directoryOutput1, mode);
            }
            inputFile.delete();

        }


    }


}
