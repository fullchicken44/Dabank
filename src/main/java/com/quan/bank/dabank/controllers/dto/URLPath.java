package com.quan.bank.dabank.controllers.dto;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class URLPath {
    private static final ImmutableList<URLPath> URL_PATHS_ACCOUNTS = ImmutableList.of();
    private String rel;
    private String href;
    private HttpMethod httpMethod;

    public static List<URLPath> getPathForAccount() {
        return URL_PATHS_ACCOUNTS;
    }

    public static List<URLPath> getPathForAccount(String aggregateUUID) {
        return ImmutableList.of(
                URLPath.builder()
                        .rel("self")
                        .href("/api/account/" + aggregateUUID)
                        .httpMethod(HttpMethod.GET)
                        .build(),
                URLPath.builder()
                        .rel("self")
                        .href("/api/account/" + aggregateUUID + "/changeName")
                        .httpMethod(HttpMethod.PUT)
                        .build()
        );
    }
    public static List<URLPath> getPathForAccount(UUID aggregateUUID) {
        return getPathForAccount(aggregateUUID.toString());
    }
}
