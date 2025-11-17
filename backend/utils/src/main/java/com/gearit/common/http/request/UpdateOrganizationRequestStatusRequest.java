package com.gearit.common.http.request;

import com.gearit.common.dto.OrganizationRequestStatusDto;

public record UpdateOrganizationRequestStatusRequest(
        OrganizationRequestStatusDto status
) {
}
