/*
 * This file is generated by jOOQ.
 */
package com.emented.weblab4.jooq.tables;


import com.emented.weblab4.jooq.Keys;
import com.emented.weblab4.jooq.S336189;
import com.emented.weblab4.jooq.tables.records.RolesUsersRelationRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class RolesUsersRelation extends TableImpl<RolesUsersRelationRecord> {

    /**
     * The reference instance of <code>s336189.roles_users_relation</code>
     */
    public static final RolesUsersRelation ROLES_USERS_RELATION = new RolesUsersRelation();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>s336189.roles_users_relation.user_id</code>.
     */
    public final TableField<RolesUsersRelationRecord, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>s336189.roles_users_relation.role_id</code>.
     */
    public final TableField<RolesUsersRelationRecord, Integer> ROLE_ID = createField(DSL.name("role_id"), SQLDataType.INTEGER.nullable(false), this, "");
    private transient Users _users;
    private transient Roles _roles;

    private RolesUsersRelation(Name alias, Table<RolesUsersRelationRecord> aliased) {
        this(alias, aliased, null);
    }

    private RolesUsersRelation(Name alias, Table<RolesUsersRelationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>s336189.roles_users_relation</code> table reference
     */
    public RolesUsersRelation(String alias) {
        this(DSL.name(alias), ROLES_USERS_RELATION);
    }

    /**
     * Create an aliased <code>s336189.roles_users_relation</code> table reference
     */
    public RolesUsersRelation(Name alias) {
        this(alias, ROLES_USERS_RELATION);
    }

    /**
     * Create a <code>s336189.roles_users_relation</code> table reference
     */
    public RolesUsersRelation() {
        this(DSL.name("roles_users_relation"), null);
    }

    public <O extends Record> RolesUsersRelation(Table<O> child, ForeignKey<O, RolesUsersRelationRecord> key) {
        super(child, key, ROLES_USERS_RELATION);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RolesUsersRelationRecord> getRecordType() {
        return RolesUsersRelationRecord.class;
    }

    @Override
    public Schema getSchema() {
        return S336189.S336189;
    }

    @Override
    public List<UniqueKey<RolesUsersRelationRecord>> getKeys() {
        return Arrays.<UniqueKey<RolesUsersRelationRecord>>asList(Keys.ROLES_USERS_RELATION_USER_ID_ROLE_ID_KEY);
    }

    @Override
    public List<ForeignKey<RolesUsersRelationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RolesUsersRelationRecord, ?>>asList(Keys.ROLES_USERS_RELATION__FK_ROLES_USERS_RELATION_USER_ID, Keys.ROLES_USERS_RELATION__FK_ROLES_USERS_RELATION_ROLE_ID);
    }

    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.ROLES_USERS_RELATION__FK_ROLES_USERS_RELATION_USER_ID);

        return _users;
    }

    public Roles roles() {
        if (_roles == null)
            _roles = new Roles(this, Keys.ROLES_USERS_RELATION__FK_ROLES_USERS_RELATION_ROLE_ID);

        return _roles;
    }

    @Override
    public RolesUsersRelation as(String alias) {
        return new RolesUsersRelation(DSL.name(alias), this);
    }

    @Override
    public RolesUsersRelation as(Name alias) {
        return new RolesUsersRelation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RolesUsersRelation rename(String name) {
        return new RolesUsersRelation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RolesUsersRelation rename(Name name) {
        return new RolesUsersRelation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
