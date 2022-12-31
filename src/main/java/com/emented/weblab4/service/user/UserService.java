package com.emented.weblab4.service.user;

import com.emented.weblab4.DTO.UserCredentialsDTO;

public interface UserService {

    void registerUser(UserCredentialsDTO userCredentialsDTO, String url);

    String loginUser(UserCredentialsDTO userCredentialsDTO);

    void logoutUser(Integer userId);

    void verifyUser(String verificationCode);

}
