package com.emented.weblab4.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCredentialsDTO {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Size(min = 5, max = 25)
    private final String password;


}
