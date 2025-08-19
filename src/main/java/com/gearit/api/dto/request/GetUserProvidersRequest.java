package com.gearit.api.dto.request;

import org.springframework.data.domain.Pageable;

public record GetUserProvidersRequest(Pageable pageable) {
}
