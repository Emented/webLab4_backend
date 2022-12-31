package com.emented.weblab4.service.hit;

import com.emented.weblab4.DAO.HitCheck;
import com.emented.weblab4.DTO.HitCheckDTO;
import org.springframework.stereotype.Service;


@Service
public class HitCheckerImpl implements HitChecker {

    @Override
    public boolean checkHit(HitCheck hitCheck) {
        return checkInFirstQuoterTriangle(hitCheck)
                || checkInThirdQuoterSquare(hitCheck)
                || checkInFourthQuoterCircle(hitCheck);
    }

    private boolean checkInFirstQuoterTriangle(HitCheck hitCheck) {
        return hitCheck.getX() >= 0
                && hitCheck.getY() >= 0
                && hitCheck.getY() <= hitCheck.getR() - hitCheck.getX();
    }

    private boolean checkInThirdQuoterSquare(HitCheck hitCheck) {
        return hitCheck.getX() <= 0
                && hitCheck.getY() <= 0
                && hitCheck.getX() >= -hitCheck.getR()
                && hitCheck.getY() >= -hitCheck.getR();
    }

    private boolean checkInFourthQuoterCircle(HitCheck hitCheck) {
        return hitCheck.getX() >= 0
                && hitCheck.getY() <= 0
                && Math.pow(hitCheck.getX(), 2) + Math.pow(hitCheck.getY(), 2) <= Math.pow(hitCheck.getR(), 2);
    }
}
