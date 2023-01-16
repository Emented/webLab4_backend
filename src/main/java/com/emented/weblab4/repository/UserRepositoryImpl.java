package com.emented.weblab4.repository;


import com.emented.weblab4.jooq.tables.records.UsersRecord;
import com.emented.weblab4.mapper.UserMapper;
import com.emented.weblab4.model.Role;
import com.emented.weblab4.model.User;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.jooq.TableField;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.emented.weblab4.jooq.Tables.*;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dslContext;
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(DSLContext dslContext, UserMapper userMapper) {
        this.dslContext = dslContext;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Optional<User> findUserByEmail(String email) {

        return findByField(email, USERS.EMAIL);

    }

    @Override
    @Transactional
    public Optional<User> findUserByVerificationCode(String verificationCode) {

        return findByField(verificationCode, USERS.VERIFICATION_CODE);

    }

    @Override
    @Transactional
    public Optional<User> findUserById(Integer userId) {

        return findByField(userId, USERS.ID);

    }

    private <T> Optional<User> findByField(T value, TableField<UsersRecord, T> field) {

        return dslContext.select(USERS.ID,
                        USERS.EMAIL,
                        USERS.PASSWORD,
                        USERS.VERIFICATION_CODE,
                        USERS.ENABLED,
                        multiset(
                                select(ROLES.ID, ROLES.NAME)
                                        .from(ROLES)
                                        .innerJoin(ROLES_USERS_RELATION)
                                        .on(ROLES_USERS_RELATION.ROLE_ID.eq(ROLES.ID))
                                        .where(ROLES_USERS_RELATION.USER_ID.eq(USERS.ID))
                        ).convertFrom(record -> record.map(Records.mapping(Role::new)))
                )
                .from(USERS)
                .where(field.eq(value))
                .fetchOptional(Records.mapping(User::new));

    }

    @Override
    @Transactional
    public Integer saveUser(User user) {
        Integer newUserID = dslContext.insertInto(USERS)
                .set(userMapper.unmap(user))
                .returning(USERS.ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Troubles during insert"))
                .get(USERS.ID);

        for (Role role : user.getRoles()) {
            dslContext.insertInto(ROLES_USERS_RELATION, ROLES_USERS_RELATION.ROLE_ID, ROLES_USERS_RELATION.USER_ID)
                    .values(dslContext.select(ROLES.ID)
                            .from(ROLES)
                            .where(ROLES.NAME.eq(role.getName()))
                            .fetchOptional()
                            .orElseThrow(() -> new DataAccessException("Troubles during insert"))
                            .get(ROLES.ID), newUserID)
                    .execute();
        }

        return newUserID;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        dslContext.deleteFrom(ROLES_USERS_RELATION)
                .where(ROLES_USERS_RELATION.USER_ID.eq(id))
                .execute();

        dslContext.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

    @Override
    @Transactional
    public void updateUser(User user) {

        for (Role role : user.getRoles()) {
            dslContext.insertInto(ROLES_USERS_RELATION, ROLES_USERS_RELATION.ROLE_ID, ROLES_USERS_RELATION.USER_ID)
                    .values(role.getId() == null ? dslContext.select(ROLES.ID)
                            .from(ROLES)
                            .where(ROLES.NAME.eq(role.getName()))
                            .fetchOptional()
                            .orElseThrow(() -> new DataAccessException("Troubles during insert"))
                            .get(ROLES.ID) : role.getId(), user.getId())
                    .onConflictDoNothing()
                    .execute();
        }

        dslContext.update(USERS)
                .set(userMapper.unmap(user))
                .where(USERS.EMAIL.eq(user.getEmail()))
                .execute();

    }

}
