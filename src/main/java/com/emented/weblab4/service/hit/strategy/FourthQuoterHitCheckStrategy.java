package com.emented.weblab4.service.hit.strategy;

import com.emented.weblab4.DAO.HitCheck;
import org.springframework.stereotype.Service;

@Service
public class FourthQuoterHitCheckStrategy implements HitCheckStrategy {

    @Override
    public boolean checkHit(HitCheck hitCheck) {
        return hitCheck.getX() >= 0
                && hitCheck.getY() <= 0
                && Math.pow(hitCheck.getX(), 2) + Math.pow(hitCheck.getY(), 2) <= Math.pow(hitCheck.getR(), 2);
    }
}
