package com.emented.weblab4.mapper;

import com.emented.weblab4.jooq.tables.records.RolesRecord;
import com.emented.weblab4.model.Role;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements RecordMapper<RolesRecord, Role>, RecordUnmapper<Role, RolesRecord> {


    @Override
    public Role map(RolesRecord rolesRecord) {
        Role role = new Role();

        role.setId(rolesRecord.getId());
        role.setName(rolesRecord.getName());

        return role;
    }

    @Override
    public RolesRecord unmap(Role role) throws MappingException {
        RolesRecord rolesRecord = new RolesRecord();

        rolesRecord.setName(role.getName());

        return rolesRecord;
    }
}
