package com.quan.bank.dabank.controllers;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.Gson;
import com.quan.bank.dabank.aggregates.AccountService;
import com.quan.bank.dabank.aggregates.EventStorage;
import com.quan.bank.dabank.controllers.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.quan.bank.dabank.controllers.dto.URLPath.getPathForAccount;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private static final String VALIDATION_ERROR = "Validation errors :>";
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

    @GetMapping("/{uuid}")
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

    @PostMapping("/createAccount")
    public ResponseEntity<APIResponse> createAccount(@RequestBody Object request) {
        try {
            CreateAccountRequest payload = GSON.fromJson(request.toString(), CreateAccountRequest.class);
            ListMultimap<String, String> validationErrors = validationErrorsMap();
            validateString("fullName", payload.getFullName(), validationErrors);
            if (!validationErrors.isEmpty()) {
                return ResponseEntity.badRequest().body(new APIResponse(APIResponse.Status.ERROR, VALIDATION_ERROR, validationErrors.asMap()));
            }

            UUID aggregateUUID = accountService.createAccountCommand(payload.getFullName());
            APIResponse apiResponse = new APIResponse(APIResponse.Status.OK, "Account created", aggregateUUID, URLPath.getPathForAccount(aggregateUUID));
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse(APIResponse.Status.ERROR, "Invalid request. Full name cannot be empty."));
        }
    }

    @PostMapping("/{uuid}/changeName")
    public ResponseEntity<APIResponse> changeFullName(@PathVariable UUID uuid, @RequestBody Object request) {
        ListMultimap<String, String> validationErrors = validationErrorsMap();
        ChangeNameRequest payload = GSON.fromJson(new Gson().toJson(request), ChangeNameRequest.class);
        validateString("fullName", payload.getFullName(), validationErrors);
        validateUUID("uuid", uuid.toString(), validationErrors);
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(new APIResponse(APIResponse.Status.ERROR, VALIDATION_ERROR, validationErrors.asMap()));
        }
        UUID aggregateUUID = UUID.fromString(uuid.toString());
        if (eventStorage.exists(aggregateUUID)) {
            accountService.changeNameCommand(aggregateUUID, payload.getFullName());
            return ResponseEntity.ok(new APIResponse(APIResponse.Status.OK, "Name changed", aggregateUUID, URLPath.getPathForAccount(aggregateUUID)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(APIResponse.Status.ERROR, "Account not found"));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<APIResponse> transferMoney(@RequestBody Object request) {
        TransferRequest payload = GSON.fromJson(request.toString(), TransferRequest.class);
        ListMultimap<String, String> validationErrors = validationErrorsMap();
        String fromUUID = payload.getFromUUID();
        String toUUID = payload.getToUUID();
        validateUUID("fromUUID", fromUUID, validationErrors);
        validateUUID("toUUID", toUUID, validationErrors);

        // Validate transfer to same account, transfer amount <= 0
        if (fromUUID != null && toUUID != null && fromUUID.equals(toUUID)) {
            validationErrors.put(
                    "toAccountNumber", "Invalid transfer request, cannot transfer to the same account"
            );
        }
        if (payload.getTransferValue() == null || payload.getTransferValue().compareTo(BigDecimal.ZERO) <= 0) {
            validationErrors.put("value", "Transfer amount must be greater than 0");
        }
        if (!validationErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse(APIResponse.Status.ERROR, VALIDATION_ERROR, validationErrors.asMap()));
        }

        // Check if those ids exist
        if (!eventStorage.exists(UUID.fromString(toUUID))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(APIResponse.Status.ERROR, "Account with id: " + toUUID + " not exist"));
        }
        if (!eventStorage.exists(UUID.fromString(fromUUID))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(APIResponse.Status.ERROR, "Account with id: " + fromUUID + " not exist"));
        }

        accountService.moneyTransferCommand(UUID.fromString(fromUUID), UUID.fromString(toUUID), payload.getTransferValue());
        return ResponseEntity.ok(new APIResponse(("Money transferred"), getPathForAccount()));
    }

}
