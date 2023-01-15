package com.emented.weblab4.repository;

import com.emented.weblab4.jooq.Tables;
import com.emented.weblab4.mapper.RoleMapper;
import com.emented.weblab4.model.Role;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final DSLContext dslContext;

    private final RoleMapper roleMapper;

    @Autowired
    public RoleRepositoryImpl(DSLContext dslContext, RoleMapper roleMapper) {
        this.dslContext = dslContext;
        this.roleMapper = roleMapper;
    }

    @Override
    public Integer saveRole(Role role) {
        return dslContext.insertInto(Tables.ROLES)
                .set(roleMapper.unmap(role))
                .onConflictDoNothing()
                .returning(Tables.ROLES.ID)
                .fetchOptional()
                .orElseGet(() -> roleMapper.unmap(role))
                .get(Tables.ROLES.ID);
    }

}
