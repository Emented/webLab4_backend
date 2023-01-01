package com.emented.weblab4.sequrity.service;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil,
                            JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    private static Optional<String> getTokenHeader(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            return Optional.of(requestTokenHeader.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain) throws ServletException, IOException {

        Optional<String> requestTokenHeaderOptional = getTokenHeader(request);

        String username = null;

        if (requestTokenHeaderOptional.isPresent()) {

            String requestTokenHeader = requestTokenHeaderOptional.get();

            try {
                username = jwtTokenUtil.getUsernameFromToken(requestTokenHeader);
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
                System.out.println("JWT Token has expired");
            } catch (UnsupportedJwtException e) {
                log.error("JWT Token is not supported");
                System.out.println("JWT Token is not supported");
            } catch (MalformedJwtException | IllegalArgumentException e) {
                log.error("JWT Token is not valid");
                System.out.println("JWT Token is not valid");
            }

            try {
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(requestTokenHeader, userDetails)) {

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(usernamePasswordAuthenticationToken);
                    }

                }
            } catch (UsernameNotFoundException e) {
                log.error("User not found");
                System.out.println("User not found");
            }

        }

        chain.doFilter(request, response);
    }

}