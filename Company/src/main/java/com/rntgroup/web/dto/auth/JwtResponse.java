package com.rntgroup.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request after login")
public class JwtResponse {

    private Long id;

    private String username;

    private String accessToken;

    private String refreshToken;

}
