package org.koushik.jwtsecurityrefreshtoken01.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    @GetMapping("info")
    public String getInfo(){
        return "admin api";
    }
}
