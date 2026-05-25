package org.koushik.jwtsecurityproject.controller;

import lombok.RequiredArgsConstructor;
import org.koushik.jwtsecurityproject.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return new ResponseEntity<>(adminService.getApiInfo(), HttpStatus.OK);
    }
}
