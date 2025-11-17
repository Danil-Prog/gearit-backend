package com.gearit.api.utils.validator;

import com.gearit.api.entity.org.Organization;
import com.gearit.common.exception.WebClientException;

public class OrganizationValidator {

    private static final String ERROR_MESSAGE = "Incorrect organization details";

    public static void validate(Organization organization) {
        isValidInnKpp(organization.getInn(), organization.getKpp());
    }

    private static void isValidInnKpp(String inn, String kpp) {
        boolean innIsValid = inn.matches("^\\d{12}$");
        boolean kppIsValid = inn.matches("^\\d{9}$");

        if (!innIsValid) {
            throw asWebClientException("INN must consist of 12 digits.");
        }

        if (!kppIsValid) {
            throw asWebClientException("KPP must consist of 9 digits.");
        }
    }

    private static WebClientException asWebClientException(String extendedHelp) {
        return new WebClientException(ERROR_MESSAGE, extendedHelp);
    }
}
