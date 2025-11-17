package com.gearit.common.http.pageable.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {

    private String field;
    private FilterCondition condition;
    private Object value;

    public Filter(String field, FilterCondition condition, Object value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }
}
