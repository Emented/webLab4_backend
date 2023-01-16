package com.emented.weblab4.sequrity.service;

public interface JwtTokenUtil {

    String generateAccessToken(Integer userId);

    String generateRefreshToken(Integer userId);

    Integer getUserIdFromAccessToken(String token);

    Integer getUserIdFromRefreshToken(String token);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);


}
