package com.gearit.common.http.pageable.filter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterContainer {

    private List<Filter> filters;

    public FilterContainer(List<Filter> filters) {
        this.filters = filters;
    }
}