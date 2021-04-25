package com.simple.rest.service.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptionPasswords {
	

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String bytetoString(byte[] input) {
        String s = new String(input, StandardCharsets.UTF_8);
        return s;
    }

    public static byte[] getHashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(stringToByte(password));
        return hashedBytes;
    }

    public static byte[] stringToByte(String input) {
        return input.getBytes();
    }

}
