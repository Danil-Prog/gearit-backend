package com.gearit.api.utils.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T> {

    private String status;
    private String result;
    private ResponseError error;
    private T data;
}
