package com.gearit.common.http.request;

import com.gearit.common.dto.OrganizationAddressDto;

import java.time.Instant;

public record NewOrganizationRequest(
        String fullname,
        String abbreviatedName,
        String description,
        OrganizationAddressDto registeredAddress,
        OrganizationAddressDto currentAddress,
        String phone,
        String inn,
        String kpp,
        Instant createdAtOrganization
) {
}
