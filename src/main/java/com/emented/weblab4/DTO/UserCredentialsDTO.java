package com.emented.weblab4.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentialsDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 5, max = 25)
    private String password;


}
