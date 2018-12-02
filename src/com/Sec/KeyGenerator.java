package com.Sec;

import com.sun.crypto.provider.PBKDF2HmacSHA1Factory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class KeyGenerator {
    private byte[] salt = {1, 2, 3, 4, 5, 6, 7, 8};

    protected byte[] generateKey(String password, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength * 8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }


}

