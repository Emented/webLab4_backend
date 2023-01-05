package com.emented.weblab4.repository;


import com.emented.weblab4.DAO.User;
import com.emented.weblab4.jooq.Tables;
import com.emented.weblab4.mapper.UserMapper;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dslContext;
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(DSLContext dslContext, UserMapper userMapper) {
        this.dslContext = dslContext;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.EMAIL.eq(email))
                .fetchOptional(userMapper);
    }

    @Override
    public Optional<User> findUserByVerificationCode(String verificationCode) {
        return dslContext.selectFrom(Tables.USERS)
                .where(Tables.USERS.VERIFICATION_CODE.eq(verificationCode))
                .fetchOptional(userMapper);
    }

    @Override
    public Integer saveUser(User user) {
        return dslContext.insertInto(Tables.USERS)
                .set(userMapper.unmap(user))
                .returning(Tables.USERS.ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Troubles during insert"))
                .get(Tables.USERS.ID);
    }

    @Override
    public void deleteUserById(Integer id) {
        dslContext.deleteFrom(Tables.USERS)
                .where(Tables.USERS.ID.eq(id))
                .execute();
    }

    @Override
    public void updateUser(User user) {
        dslContext.update(Tables.USERS)
                .set(userMapper.unmap(user))
                .where(Tables.USERS.EMAIL.eq(user.getEmail()))
                .execute();
    }

}
