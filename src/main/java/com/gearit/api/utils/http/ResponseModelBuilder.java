package com.gearit.api.utils.http;

public class ResponseModelBuilder<T> {

    private final ResponseModel<T> response = new ResponseModel<>();

    public ResponseModelBuilder<T> success() {
        return new ResponseModelBuilder<T>().result("Succeed").status("200");
    }

    public ResponseModelBuilder<T> failure() {
        return new ResponseModelBuilder<T>().result("Failed").status("200");
    }

    public ResponseModelBuilder<T> error(ResponseError error) {
        response.setError(error);
        response.setResult("Failed");
        response.setStatus("500");
        return this;
    }

    public ResponseModelBuilder<T> addData(final T body) {
        response.setData(body);
        response.setResult("Succeed");
        response.setStatus("200");
        return this;
    }

    public ResponseModel<T> build() {
        return response;
    }

    private ResponseModelBuilder<T> result(String result) {
        response.setResult(result);
        return this;
    }

    private ResponseModelBuilder<T> status(String status) {
        response.setStatus(status);
        return this;
    }
}
