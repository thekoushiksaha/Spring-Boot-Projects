package org.koushik.authappbackend.dto;

import lombok.*;
import org.koushik.authappbackend.model.Provider;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String imageUrl;
    private Boolean enabled;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles = new HashSet<>();
}
