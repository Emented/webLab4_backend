package com.emented.weblab4.DAO;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String email;
    private String password;
    private String verificationCode;
    private boolean enabled;

}
