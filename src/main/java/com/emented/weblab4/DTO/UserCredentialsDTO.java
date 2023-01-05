package com.emented.weblab4.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
