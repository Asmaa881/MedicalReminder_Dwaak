package com.example.mediacalremider;

import java.util.regex.Pattern;

public class ValidationClass {

    private static final String Email_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN
            = Pattern.compile(Email_REGEXP);



    // Return true when *email* is email
    public static  boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
