package com.emented.weblab4.service.hit;


import com.emented.weblab4.DTO.HitCheckDTO;
import com.emented.weblab4.model.HitCheck;
import com.emented.weblab4.repository.HitCheckRepository;
import com.emented.weblab4.service.hit.strategy.HitCheckStrategy;
import org.jooq.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HitServiceImpl implements HitService {

    private final HitCheckRepository hitCheckRepository;
    private final List<HitCheckStrategy> hitCheckStrategyList;
    private final Converter<HitCheck, HitCheckDTO> converter;

    @Autowired
    public HitServiceImpl(HitCheckRepository hitCheckRepository,
                          List<HitCheckStrategy> hitCheckStrategyList,
                          Converter<HitCheck, HitCheckDTO> converter) {
        this.hitCheckRepository = hitCheckRepository;
        this.hitCheckStrategyList = hitCheckStrategyList;
        this.converter = converter;
    }

    @Override
    public void checkHit(HitCheckDTO hitCheckDTO, Integer userId) {
        HitCheck hitCheck = converter.to(hitCheckDTO);

        hitCheck.setUserId(userId);
        hitCheck.setCheckDate(Instant.now());

        boolean hitCheckResult = false;

        for (HitCheckStrategy hitCheckStrategy : hitCheckStrategyList) {
            if (hitCheckStrategy.checkHit(hitCheck)) {
                hitCheckResult = true;
                break;
            }
        }

        hitCheck.setStatus(hitCheckResult);
        hitCheck.setExecutionTime((Instant.now().getNano() - hitCheck.getCheckDate().getNano()) / 1000L);

        hitCheckRepository.saveHitCheck(hitCheck);
    }

    @Override
    public void deleteHits(Integer userId) {
        hitCheckRepository.deleteAllHitChecksByUsedId(userId);
    }

    @Override
    public List<HitCheckDTO> getHitsForUserByRadius(Integer userId, Double radius) {
        List<HitCheck> hitCheckList = hitCheckRepository.findAllHitChecksByUserIdAndRadius(userId, radius);

        return hitCheckList.stream().map(converter::from).toList();
    }

    @Override
    public List<HitCheckDTO> getAllHitsForUser(Integer userId) {
        List<HitCheck> hitCheckList = hitCheckRepository.findAllHitChecksByUserId(userId);

        return hitCheckList.stream().map(converter::from).toList();
    }
}
