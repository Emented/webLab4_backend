package com.emented.weblab4.mapper;

import com.emented.weblab4.DAO.HitCheck;
import com.emented.weblab4.jooq.tables.records.HitsRecord;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class HitCheckMapper implements RecordMapper<HitsRecord, HitCheck>,
        RecordUnmapper<HitCheck, HitsRecord> {

    @Override
    public HitCheck map(HitsRecord record) {

        HitCheck hitCheck = new HitCheck();

        hitCheck.setId(record.getId());
        hitCheck.setUserId(record.getUserId());
        hitCheck.setX(record.getX());
        hitCheck.setY(record.getY());
        hitCheck.setR(record.getR());
        hitCheck.setCheckDate(record.getCheckDate().toInstant());
        hitCheck.setExecutionTime(record.getExecutionTime());
        hitCheck.setStatus(record.getStatus());

        return hitCheck;
    }

    @Override
    public HitsRecord unmap(HitCheck source) throws MappingException {

        HitsRecord hitsRecord = new HitsRecord();

        hitsRecord.setUserId(source.getUserId());
        hitsRecord.setX(source.getX());
        hitsRecord.setY(source.getY());
        hitsRecord.setR(source.getR());
        hitsRecord.setCheckDate(source.getCheckDate().atOffset(ZoneOffset.UTC));
        hitsRecord.setExecutionTime(source.getExecutionTime());
        hitsRecord.setStatus(source.getStatus());

        return hitsRecord;
    }
}
