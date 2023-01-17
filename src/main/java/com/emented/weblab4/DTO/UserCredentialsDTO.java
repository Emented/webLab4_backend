package com.emented.weblab4.DTO;


import com.emented.weblab4.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class UserCredentialsDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 5, max = 25)
    private String password;

    @Size(max = 3)
    private List<Role> roles;


}
