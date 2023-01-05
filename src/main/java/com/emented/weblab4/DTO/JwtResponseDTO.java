package com.emented.weblab4.DTO;

import lombok.Data;

@Data
public class JwtResponseDTO {

    private final String email;
    private final String accessToken;
    private final String refreshToken;

}
