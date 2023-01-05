package com.emented.weblab4.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDTO {

    @NotBlank
    private final String refreshToken;

}
