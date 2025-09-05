package com.gearit.api.exception.violation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {

}
