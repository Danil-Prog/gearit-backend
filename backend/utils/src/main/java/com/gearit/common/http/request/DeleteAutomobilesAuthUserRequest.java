package com.gearit.common.http.request;

import java.util.List;

public record DeleteAutomobilesAuthUserRequest(List<Long> automobileIds) {
}
