package com.emented.weblab4.DTO;

import lombok.Data;

@Data
public class JwtResponseDTO {

    private final Integer userId;
    private final String accessToken;
    private final String refreshToken;

}
