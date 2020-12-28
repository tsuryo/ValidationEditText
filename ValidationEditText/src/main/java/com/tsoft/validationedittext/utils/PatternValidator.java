package com.tsoft.validationedittext.utils;

import com.tsoft.validationedittext.views.ValidationEditText;

import java.util.regex.Pattern;

/**
 * Created by Tsur Yohananov on 14/10/2020.
 */
public class PatternValidator {

    private Pattern mCustomPattern;
    private ValidatorFactory.Validator mValidator;

    public PatternValidator(ValidationEditText.ValidationType type) {
        mValidator = new ValidatorFactory().getValidator(type);
    }

    public PatternValidator(ValidationEditText.ValidationType type, String regex) {
        setCustomPattern(regex);
        mValidator = new ValidatorFactory().getValidator(type, mCustomPattern);
    }

    private void setCustomPattern(String regex) {
        mCustomPattern = Pattern.compile(regex);
    }

    public boolean validate(CharSequence input) {
        return mValidator.validate(input);
    }
}
