package com.emented.weblab4.sequrity.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenUtilImpl implements Serializable, JwtTokenUtil {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access.validity}")
    private Integer accessTokenValidity;
    @Value("${jwt.refresh.validity}")
    private Integer refreshTokenValidity;

    @Override
    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenSecret, accessTokenValidity);
    }

    @Override
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenSecret, refreshTokenValidity);
    }

    private String generateToken(String username, String secret, Integer validity) {
        Date currentDate = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + validity))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, accessTokenSecret);
    }

    @Override
    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, refreshTokenSecret);
    }

    private String getUsernameFromToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessTokenSecret);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshTokenSecret);
    }

    private boolean validateToken(String token, String secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims are empty: {}", e.getMessage());
        }

        return false;
    }

}
