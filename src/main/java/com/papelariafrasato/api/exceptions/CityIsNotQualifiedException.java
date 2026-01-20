package com.papelariafrasato.api.exceptions;

public class CityIsNotQualifiedException extends RuntimeException {
    public CityIsNotQualifiedException(String city) {
        super("The city: " + city + " isn't near by Severínia");
    }
}
