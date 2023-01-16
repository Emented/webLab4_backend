package com.emented.weblab4.sequrity.bearer;

import lombok.Data;

@Data
public class CustomBearerUser {

    private final Integer userId;
    private String email;

}
