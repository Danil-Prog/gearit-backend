package com.gearit.common.http.request;

import org.springframework.data.domain.Pageable;

public record GetUserProvidersRequest(Pageable pageable) {
}
