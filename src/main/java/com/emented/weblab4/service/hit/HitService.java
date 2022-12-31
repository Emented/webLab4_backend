package com.emented.weblab4.service.hit;

import com.emented.weblab4.DTO.HitCheckDTO;

import java.util.List;

public interface HitService {

    void checkHit(HitCheckDTO hitCheckDTO, Integer userId);

    void deleteHits(Integer userId);

    List<HitCheckDTO> getHitsForUserByRadius(Integer userId, Double radius);

    List<HitCheckDTO> getAllHitsForUser(Integer userId);


}
