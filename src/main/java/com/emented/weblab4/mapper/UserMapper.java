package com.emented.weblab4.mapper;

import com.emented.weblab4.DAO.User;
import com.emented.weblab4.jooq.tables.records.UsersRecord;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements RecordMapper<UsersRecord, User>,
        RecordUnmapper<User, UsersRecord> {

    @Override
    public User map(UsersRecord record) {
        User user = new User();

        user.setId(record.getId());
        user.setEmail(record.getEmail());
        user.setPassword(record.getPassword());
        user.setEnabled(record.getEnabled());
        user.setVerificationCode(record.getVerificationCode());

        return user;
    }

    @Override
    public UsersRecord unmap(User source) throws MappingException {
        UsersRecord usersRecord = new UsersRecord();

        usersRecord.setEmail(source.getEmail());
        usersRecord.setPassword(source.getPassword());
        usersRecord.setEnabled(source.isEnabled());
        usersRecord.setVerificationCode(source.getVerificationCode());

        return usersRecord;
    }
}
