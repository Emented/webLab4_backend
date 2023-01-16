package com.emented.weblab4.sequrity.service;


import com.emented.weblab4.sequrity.jwt.BearerToken;
import com.emented.weblab4.sequrity.jwt.BearerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public JwtRequestFilter(JwtTokenUtilImpl jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
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

        if (requestTokenHeaderOptional.isPresent()) {

            try {

                String requestTokenHeader = requestTokenHeaderOptional.get();

                if (jwtTokenUtil.validateAccessToken(requestTokenHeader)) {

                    Integer userId = jwtTokenUtil.getUserIdFromAccessToken(requestTokenHeader);

                    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                        BearerToken bearerToken = new BearerToken(new BearerUser(userId));

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(bearerToken);
                    }
                }
            } catch (UsernameNotFoundException e) {
                log.error("Username not found: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

}