package com.emented.weblab4.repository;

import com.emented.weblab4.DAO.HitCheck;

import java.util.List;

public interface HitCheckRepository {

    List<HitCheck> findAllHitChecksByUserId(Integer userId);

    List<HitCheck> findAllHitChecksByUserIdAndRadius(Integer userId, Double radius);

    Integer saveHitCheck(HitCheck hitCheck);

    void deleteAllHitChecksByUsedId(Integer userId);


}
