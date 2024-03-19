package com.quan.bank.dabank.controllers.dto;

import com.quan.bank.dabank.utils.JsonUtils;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    private Status status;
    private String message;
    private Object data;
    private List<URLPath> URLPaths;

    public APIResponse(String message) {
        this(Status.OK, message, null, null);
    }

    public APIResponse(Status status, String message) {
        this(status, message, null, null);
    }

    public APIResponse(Status status, String message, Object data) {
        this(status, message, data, null);
    }

    public APIResponse(Object data, List<URLPath> URLPaths) {
        this(Status.OK, "SUCCESS", data, URLPaths);
    }

    public APIResponse(String message, List<URLPath> URLPaths) {
        this(Status.OK, message, null, URLPaths);
    }

    public String toJson() {
        return JsonUtils.toJson(this);
    }

    @AllArgsConstructor
    public enum Status {
        OK("OK"),
        ERROR("ERROR");

        private final String value;
    }
}
