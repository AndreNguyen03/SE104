package com.example.privateclinic.Controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class test {
    public static void main(String[] args) {
        System.out.println(GetHash("abc"));
    }
    public static String GetHash(String plainText) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Chuyển đổi chuỗi thành mảng bytes và băm bằng MD5
            byte[] messageDigest = md.digest(plainText.getBytes());

            // Chuyển đổi mảng bytes thành chuỗi hex để hiển thị kết quả
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Thuật toán MD5 không được hỗ trợ.");
            e.printStackTrace();
            return null;
        }
    }
}
