package com.quan.bank.dabank.controllers;

import com.quan.bank.dabank.controllers.dto.APIResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@NoArgsConstructor
@RestController
public class IndexController {

    @GetMapping("/api/health")
    public ResponseEntity<APIResponse> healthCheck() {
        return ResponseEntity.ok(new APIResponse("System's fine"));
    }
}
