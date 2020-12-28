package com.tsoft.validationedittext.utils;

import com.tsoft.validationedittext.views.ValidationEditText;

import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;
import static android.util.Patterns.IP_ADDRESS;
import static android.util.Patterns.WEB_URL;

/**
 * Created by Tsur Yohananov on 26/12/2020.
 */
public class ValidatorFactory {

    public Validator getValidator(ValidationEditText.ValidationType type) {
        return getValidator(type, null);
    }

    public Validator getValidator(ValidationEditText.ValidationType type, Pattern customPattern) {
        switch (type) {
            case EMAIL:
                return new EmailValidator();
            case PASSWORD:
                return new PasswordValidator();
            case PHONE:
                return new PhoneValidator();
            case NAME:
                return new NameValidator();
            case FULL_NAME:
                return new FullNameValidator();
            case WEB_URL:
                return new WebUrlValidator();
            case IP_ADDRESS:
                return new IPValidator();
            case CUSTOM:
                return new CustomValidator(customPattern);
        }
        throw new NullPointerException("Make sure to set pattern on ValidationEditText" +
                " Or if you'd like to use your own regex, use custom_pattern attribute");
    }

    private static class EmailValidator implements Validator {

        @Override
        public boolean validate(CharSequence sequence) {
            return EMAIL_ADDRESS.matcher(sequence).matches();
        }
    }

    private static class PasswordValidator implements Validator {
        private static final Pattern PASSWORD = Pattern.compile(
                "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,}");

        @Override
        public boolean validate(CharSequence sequence) {
            return PASSWORD.matcher(sequence).matches();
        }
    }

    private static class PhoneValidator implements Validator {
        private final Pattern mPattern;

        public PhoneValidator() {
            mPattern = Pattern.compile("[0-9]{3}[-]?[0-9]{3}[-]?[0-9]{4}");
        }

        @Override
        public boolean validate(CharSequence sequence) {
            return mPattern.matcher(sequence).matches();
        }
    }

    private static class NameValidator implements Validator {
        private static final Pattern NAME = Pattern.compile("[A-Z][a-z]+");

        @Override
        public boolean validate(CharSequence sequence) {
            return NAME.matcher(sequence).matches();
        }
    }

    private static class FullNameValidator implements Validator {
        private static final Pattern NAME = Pattern.compile("^([A-Z][a-z]*((\\s)))+[A-Z][a-z]*$");

        @Override
        public boolean validate(CharSequence sequence) {
            return NAME.matcher(sequence).matches();
        }
    }

    private static class WebUrlValidator implements Validator {

        @Override
        public boolean validate(CharSequence sequence) {
            return WEB_URL.matcher(sequence).matches();
        }
    }

    private static class IPValidator implements Validator {

        @Override
        public boolean validate(CharSequence sequence) {
            return IP_ADDRESS.matcher(sequence).matches();
        }
    }

    private static class CustomValidator implements Validator {
        private final Pattern mPattern;

        public CustomValidator(Pattern pattern) {
            mPattern = pattern;
        }

        @Override
        public boolean validate(CharSequence sequence) {
            try {
                return mPattern.matcher(sequence).matches();
            } catch (NullPointerException e) {
                throw new NullPointerException("Make sure to set " +
                        "custom_pattern on ValidationEditText with pattern=\"CUSTOM\" ");
            }
        }
    }


    public interface Validator {
        boolean validate(CharSequence sequence);
    }
}
