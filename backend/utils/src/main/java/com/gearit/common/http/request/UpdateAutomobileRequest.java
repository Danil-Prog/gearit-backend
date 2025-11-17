package com.gearit.common.http.request;

import com.gearit.common.dto.AutomobileBodyTypeDto;

public record UpdateAutomobileRequest(
        Long factoryId,
        Long modelId,
        String color,
        String license,
        AutomobileBodyTypeDto bodyType,
        Long year,
        Long odometer
) {
}
