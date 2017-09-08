package com.example.asia.jmpro.logic.validation;

import java.util.regex.Pattern;

public class EmailValidator {

    private static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validate(String email) {
        return pattern.matcher(email).matches();
    }

}
