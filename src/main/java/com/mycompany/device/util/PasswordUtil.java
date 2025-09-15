package com.mycompany.device.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class để hash password
 * @author Kim Ngan - Utility Layer
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash password với salt
     */
    public static String hashPassword(String password) {
        try {
            // Tạo salt ngẫu nhiên
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password + salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // Kết hợp salt + hashed password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
            
            // Encode base64
            return Base64.getEncoder().encodeToString(combined);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi hash password", e);
        }
    }
    
    /**
     * Verify password
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            // Decode base64
            byte[] combined = Base64.getDecoder().decode(hashedPassword);
            
            // Tách salt và hashed password
            byte[] salt = new byte[SALT_LENGTH];
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);
            
            // Hash password input với salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] inputHash = md.digest(password.getBytes());
            
            // So sánh
            return MessageDigest.isEqual(storedHash, inputHash);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi verify password", e);
        }
    }
    
    /**
     * Hash password đơn giản (không salt) - dùng cho test
     */
    public static String hashPasswordSimple(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi hash password", e);
        }
    }
    
    /**
     * Verify password đơn giản (không salt) - dùng cho test
     */
    public static boolean verifyPasswordSimple(String password, String hashedPassword) {
        String inputHash = hashPasswordSimple(password);
        return inputHash.equals(hashedPassword);
    }
}
