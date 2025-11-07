package com.gearit.common.http.request;

import com.gearit.common.dto.AutomobileBodyTypeDto;

public record CreateAutomobileRequest(
        Long factoryId,
        Long modelId,
        String color,
        String license,
        AutomobileBodyTypeDto bodyType
) {
}
