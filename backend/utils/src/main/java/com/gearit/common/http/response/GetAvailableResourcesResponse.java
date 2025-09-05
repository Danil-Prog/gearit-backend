package com.gearit.common.http.response;

import java.util.Set;

public record GetAvailableResourcesResponse(Set<String> availableResources) {
}
