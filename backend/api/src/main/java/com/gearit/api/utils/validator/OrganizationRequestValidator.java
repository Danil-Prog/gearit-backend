package com.gearit.api.utils.validator;

import com.gearit.api.entity.org.OrganizationRequest;
import com.gearit.common.exception.WebClientException;

public class OrganizationRequestValidator {

    private static final String ERROR_MESSAGE = "Incorrect organization details";

    public static void validate(OrganizationRequest organization) {
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
