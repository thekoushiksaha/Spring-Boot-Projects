package org.koushik.jwtsecurityrefreshtoken01.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    @NotBlank(message = "username cannot be blank")
    private String username;
}
