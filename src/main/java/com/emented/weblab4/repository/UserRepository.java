package com.emented.weblab4.repository;

import com.emented.weblab4.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByVerificationCode(String verificationCode);

    Integer saveUser(User user);

    void deleteUserById(Integer id);

    void updateUser(User user);

}
