package com.gearit.api.dto.request;

import java.util.List;

public record CreateAccessPolicyRequest(String name, List<String> resources) {
}
