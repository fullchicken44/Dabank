package com.quan.bank.dabank.controllers;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.Gson;
import com.quan.bank.dabank.aggregates.AccountService;
import com.quan.bank.dabank.aggregates.EventStorage;
import com.quan.bank.dabank.controllers.dto.APIResponse;
import com.quan.bank.dabank.controllers.dto.AccountResponse;
import com.quan.bank.dabank.controllers.dto.CreateAccountRequest;
import com.quan.bank.dabank.controllers.dto.URLPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.quan.bank.dabank.controllers.dto.URLPath.getPathForAccount;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private static final String VALIDATION_ERROR = "Validation errors";
    private static final Gson GSON = new Gson();
    private final AccountService accountService;
    private final EventStorage eventStorage;

    @Autowired
    public AccountController(AccountService accountService, EventStorage eventStorage) {
        this.accountService = accountService;
        this.eventStorage = eventStorage;
    }

    private static boolean isUUIDNotValid(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            UUID.fromString(value);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static ListMultimap<String, String> validationErrorsMap() {
        return MultimapBuilder.hashKeys().arrayListValues().build();
    }

    private static void validateString(
            String fieldName, String value, ListMultimap<String, String> validationErrors) {
        if (value == null || value.isEmpty()) {
            validationErrors.put(fieldName, "Cannot be empty");
        }
    }

    private static void validateUUID(
            String fieldName, String value, ListMultimap<String, String> validationErrors) {
        if (isUUIDNotValid(value)) {
            validationErrors.put(fieldName, "Is not a valid UUID value");
        }
    }

    @GetMapping("/listAccounts")
    public ResponseEntity<APIResponse> listAccount() {
        return ResponseEntity.ok(new APIResponse(eventStorage.findAll().stream().map(AccountResponse::from).collect(toImmutableList()), getPathForAccount()));
    }

    @GetMapping("/getAccount/{uuid}")
    public ResponseEntity<APIResponse> getAccount(@PathVariable("uuid") String uuid) {
        ListMultimap<String, String> validationErrors = validationErrorsMap();
        validateUUID("uuid", uuid, validationErrors);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(new APIResponse(APIResponse.Status.ERROR, VALIDATION_ERROR, validationErrors.asMap()));
        }
        UUID aggregateUUID = UUID.fromString(uuid);
        if (eventStorage.exists(aggregateUUID)) {
            return ResponseEntity.ok(new APIResponse(AccountResponse.from(eventStorage.get(aggregateUUID)), getPathForAccount()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(APIResponse.Status.ERROR, "Account not found"));
        }
    }

    @GetMapping("/createAccount")
    public ResponseEntity<APIResponse> createAccount(@RequestBody Object request) {
        CreateAccountRequest payload = GSON.fromJson(request.toString(), CreateAccountRequest.class);
        ListMultimap<String, String> validationErrors = validationErrorsMap();
        validateString("fullName", payload.getFullName(), validationErrors);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(new APIResponse(APIResponse.Status.ERROR, VALIDATION_ERROR, validationErrors.asMap()));
        }

        UUID aggregateUUID = accountService.createAccountCommand(payload.getFullName());
        APIResponse apiResponse = new APIResponse(APIResponse.Status.OK, "Account created", aggregateUUID, URLPath.getPathForAccount(aggregateUUID));
        return ResponseEntity.ok(apiResponse);
    }


}
