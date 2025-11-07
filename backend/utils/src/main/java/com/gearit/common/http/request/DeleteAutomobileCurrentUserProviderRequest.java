package com.gearit.common.http.request;

import java.util.List;

public record DeleteAutomobileCurrentUserProviderRequest(List<Long> automobileIds) {
}
