package org.koushik.jwtsecurityproject.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminControllerImpl implements AdminService {
    @Override
    public Map<String, String> getApiInfo() {
        return Map.of("api_info","admin_api");
    }
}
