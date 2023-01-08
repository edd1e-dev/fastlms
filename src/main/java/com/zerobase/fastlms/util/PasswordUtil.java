package com.zerobase.fastlms.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    public static boolean equals(String plain, String hashed) {
        if (plain == null || plain.length() < 1) {
            return false;
        }

        if (hashed == null || hashed.length() < 1) {
            return false;
        }

        return BCrypt.checkpw(plain, hashed);
    }

    public static String encryptPassword(String plain) {
        if (plain == null || plain.length() < 1) {
            return "";
        }

        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }
}
