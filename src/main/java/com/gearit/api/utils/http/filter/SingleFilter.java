package com.gearit.api.utils.http.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleFilter {

    private String field;
    private SingleCondition condition;
    private Object value;

    public SingleFilter(String field, SingleCondition condition, Object value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }
}
