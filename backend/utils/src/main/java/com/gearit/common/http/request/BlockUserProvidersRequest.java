package com.gearit.common.http.request;

import java.util.List;

public record BlockUserProvidersRequest(List<Long> ids) {
}
