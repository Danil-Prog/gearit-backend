package com.gearit.api.dto.response;

import java.util.Set;

public record GetAvailableResourcesResponse(Set<String> availableResources) {
}
