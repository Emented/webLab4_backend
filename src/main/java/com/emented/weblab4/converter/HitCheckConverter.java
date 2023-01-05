package com.emented.weblab4.converter;


import com.emented.weblab4.DAO.HitCheck;
import com.emented.weblab4.DTO.HitCheckDTO;
import org.jooq.Converter;
import org.springframework.stereotype.Component;

@Component
public class HitCheckConverter implements Converter<HitCheck, HitCheckDTO> {


    @Override
    public HitCheckDTO from(HitCheck databaseObject) {
        HitCheckDTO hitCheckDTO = new HitCheckDTO();

        hitCheckDTO.setId(databaseObject.getId());
        hitCheckDTO.setX(databaseObject.getX());
        hitCheckDTO.setY(databaseObject.getY());
        hitCheckDTO.setR(databaseObject.getR());
        hitCheckDTO.setCheckDate(databaseObject.getCheckDate());
        hitCheckDTO.setExecutionTime(databaseObject.getExecutionTime());
        hitCheckDTO.setStatus(databaseObject.getStatus());

        return hitCheckDTO;
    }

    @Override
    public HitCheck to(HitCheckDTO userObject) {
        HitCheck hitCheck = new HitCheck();

        hitCheck.setX(userObject.getX());
        hitCheck.setY(userObject.getY());
        hitCheck.setR(userObject.getR());

        return hitCheck;
    }

    @Override
    public Class<HitCheck> fromType() {
        return HitCheck.class;
    }

    @Override
    public Class<HitCheckDTO> toType() {
        return HitCheckDTO.class;
    }
}
