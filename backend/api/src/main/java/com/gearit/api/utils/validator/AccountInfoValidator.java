package com.gearit.api.utils.validator;

import com.gearit.api.entity.account.AccountGender;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.common.exception.WebClientException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gearit.common.utils.StringUtils.stringNullOrBlank;

public class AccountInfoValidator {

    private static final String ERROR_MESSAGE = "Incorrect account details";

    public static void validate(AccountInfo accountInfo) {
        isValidaFullName(accountInfo.getFirstName(), accountInfo.getMiddleName(), accountInfo.getLastName());
        isValidPhoneNumber(accountInfo.getPhoneNumber());
        isValidGender(accountInfo.getGender());
    }

    private static void isValidaFullName(String firstName, String middleName, String lastName) {
        if (stringNullOrBlank(firstName)) {
            throw asWebClientException("Firstname cannot be empty");
        }

        if (stringNullOrBlank(middleName)) {
            throw asWebClientException("Middle name cannot be empty");
        }

        if (stringNullOrBlank(lastName)) {
            throw asWebClientException("Last name cannot be empty");
        }
    }

    private static void isValidPhoneNumber(String phoneNumber) {
        if (stringNullOrBlank(phoneNumber)) {
            throw asWebClientException("Phone number cannot be empty");
        }

        Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        Matcher matcher = pattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            throw asWebClientException("Invalid phone number format");
        }
    }

    private static void isValidGender(AccountGender gender) {
        if (gender == null) {
            throw asWebClientException("Gender cannot be empty");
        }
    }

    private static WebClientException asWebClientException(String extendedHelp) {
        return new WebClientException(ERROR_MESSAGE, extendedHelp);
    }
}
