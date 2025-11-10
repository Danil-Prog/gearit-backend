package com.gearit.common.http.request;

public record RegisterOrganizationRequest(
        String fullname,
        String abbreviatedName,
        String description,
        String registeredAddress,
        String currentAddress,
        String phone,
        String inn,
        String kpp,
        Long year
) {
}
