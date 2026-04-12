package com.example.demo.utils;

public final class StringUtils {

    private StringUtils() {
        // Private constructor to prevent instantiation
    }

    public static boolean isPalindrome(String str) {
        if (str == null) {
            return false;
        }
        String cleaned = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return cleaned.contentEquals(new StringBuilder(cleaned).reverse());
    }
}
