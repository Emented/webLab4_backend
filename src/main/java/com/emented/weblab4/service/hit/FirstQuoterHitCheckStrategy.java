package com.emented.weblab4.service.hit;

import com.emented.weblab4.DAO.HitCheck;
import org.springframework.stereotype.Service;

@Service
public class FirstQuoterHitCheckStrategy implements HitCheckStrategy {

    @Override
    public boolean checkHit(HitCheck hitCheck) {
        return hitCheck.getX() >= 0
                && hitCheck.getY() >= 0
                && hitCheck.getY() <= hitCheck.getR() - hitCheck.getX();
    }
}
