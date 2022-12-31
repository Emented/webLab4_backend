package com.emented.weblab4.repository;

import com.emented.weblab4.DAO.HitCheck;
import com.emented.weblab4.jooq.Tables;
import com.emented.weblab4.mapper.HitCheckMapper;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HitCheckRepositoryImpl implements HitCheckRepository {

    private final DSLContext dslContext;
    private final HitCheckMapper hitCheckMapper;

    @Autowired
    public HitCheckRepositoryImpl(DSLContext dslContext, HitCheckMapper hitCheckMapper) {
        this.dslContext = dslContext;
        this.hitCheckMapper = hitCheckMapper;
    }

    @Override
    public List<HitCheck> findAllHitChecksByUserId(Integer userId) {
        return dslContext.selectFrom(Tables.HITS)
                .where(Tables.HITS.USER_ID.eq(userId))
                .fetch(hitCheckMapper);
    }

    @Override
    public List<HitCheck> findAllHitChecksByUserIdAndRadius(Integer userId, Double radius) {
        return dslContext.selectFrom(Tables.HITS)
                .where(Tables.HITS.USER_ID.eq(userId)
                        .and(Tables.HITS.R.eq(radius)))
                .fetch(hitCheckMapper);
    }

    @Override
    public Integer saveHitCheck(HitCheck hitCheck) {
        return dslContext.insertInto(Tables.HITS)
                .set(hitCheckMapper.unmap(hitCheck))
                .returning(Tables.HITS.ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Troubles during insert"))
                .get(Tables.HITS.ID);
    }

    @Override
    public void deleteAllHitChecksByUsedId(Integer userId) {
        dslContext.deleteFrom(Tables.HITS)
                .where(Tables.HITS.USER_ID.eq(userId))
                .execute();
    }

}
