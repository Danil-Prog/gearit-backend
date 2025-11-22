package com.gearit.common.dto;

public record OrganizationAddressDto(
        Long id,
        Long index,
        String country,
        String region,
        String street,
        String city,
        String building,
        String placement
) {
}
