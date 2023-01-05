package com.emented.weblab4.sequrity.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtRequestFilter(JwtTokenUtilImpl jwtTokenUtil,
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

        String username;

        if (requestTokenHeaderOptional.isPresent()) {

            try {

                String requestTokenHeader = requestTokenHeaderOptional.get();

                if (jwtTokenUtil.validateAccessToken(requestTokenHeader)) {

                    username = jwtTokenUtil.getUsernameFromAccessToken(requestTokenHeader);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

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
                log.error("Username not found: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

}