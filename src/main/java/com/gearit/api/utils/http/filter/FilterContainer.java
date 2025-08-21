package com.gearit.api.utils.http.filter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterContainer {

    private List<SingleFilter> filters;

    public FilterContainer(List<SingleFilter> filters) {
        this.filters = filters;
    }
}