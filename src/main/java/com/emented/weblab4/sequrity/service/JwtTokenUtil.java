package com.emented.weblab4.sequrity.service;

public interface JwtTokenUtil {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    String getUsernameFromAccessToken(String token);

    String getUsernameFromRefreshToken(String token);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);


}
