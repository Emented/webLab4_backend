/*
 * This file is generated by jOOQ.
 */
package com.emented.weblab4.jooq.tables;


import com.emented.weblab4.jooq.Keys;
import com.emented.weblab4.jooq.S336189;
import com.emented.weblab4.jooq.tables.records.HitsRecord;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Hits extends TableImpl<HitsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>s336189.hits</code>
     */
    public static final Hits HITS = new Hits();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<HitsRecord> getRecordType() {
        return HitsRecord.class;
    }

    /**
     * The column <code>s336189.hits.id</code>.
     */
    public final TableField<HitsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>s336189.hits.user_id</code>.
     */
    public final TableField<HitsRecord, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>s336189.hits.x</code>.
     */
    public final TableField<HitsRecord, Double> X = createField(DSL.name("x"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>s336189.hits.y</code>.
     */
    public final TableField<HitsRecord, Double> Y = createField(DSL.name("y"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>s336189.hits.r</code>.
     */
    public final TableField<HitsRecord, Double> R = createField(DSL.name("r"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>s336189.hits.check_date</code>.
     */
    public final TableField<HitsRecord, OffsetDateTime> CHECK_DATE = createField(DSL.name("check_date"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>s336189.hits.execution_time</code>.
     */
    public final TableField<HitsRecord, Long> EXECUTION_TIME = createField(DSL.name("execution_time"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>s336189.hits.status</code>.
     */
    public final TableField<HitsRecord, Boolean> STATUS = createField(DSL.name("status"), SQLDataType.BOOLEAN.nullable(false), this, "");

    private Hits(Name alias, Table<HitsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Hits(Name alias, Table<HitsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>s336189.hits</code> table reference
     */
    public Hits(String alias) {
        this(DSL.name(alias), HITS);
    }

    /**
     * Create an aliased <code>s336189.hits</code> table reference
     */
    public Hits(Name alias) {
        this(alias, HITS);
    }

    /**
     * Create a <code>s336189.hits</code> table reference
     */
    public Hits() {
        this(DSL.name("hits"), null);
    }

    public <O extends Record> Hits(Table<O> child, ForeignKey<O, HitsRecord> key) {
        super(child, key, HITS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : S336189.S336189;
    }

    @Override
    public Identity<HitsRecord, Integer> getIdentity() {
        return (Identity<HitsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<HitsRecord> getPrimaryKey() {
        return Keys.HITS_PKEY;
    }

    @Override
    public List<ForeignKey<HitsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.HITS__FK_USER);
    }

    private transient Users _users;

    /**
     * Get the implicit join path to the <code>s336189.users</code> table.
     */
    public Users users() {
        if (_users == null)
            _users = new Users(this, Keys.HITS__FK_USER);

        return _users;
    }

    @Override
    public Hits as(String alias) {
        return new Hits(DSL.name(alias), this);
    }

    @Override
    public Hits as(Name alias) {
        return new Hits(alias, this);
    }

    @Override
    public Hits as(Table<?> alias) {
        return new Hits(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Hits rename(String name) {
        return new Hits(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Hits rename(Name name) {
        return new Hits(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Hits rename(Table<?> name) {
        return new Hits(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, Integer, Double, Double, Double, OffsetDateTime, Long, Boolean> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super Integer, ? super Integer, ? super Double, ? super Double, ? super Double, ? super OffsetDateTime, ? super Long, ? super Boolean, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super Integer, ? super Integer, ? super Double, ? super Double, ? super Double, ? super OffsetDateTime, ? super Long, ? super Boolean, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
