package com.emented.weblab4.controller;

import com.emented.weblab4.DTO.*;
import com.emented.weblab4.sequrity.bearer.CustomBearerUser;
import com.emented.weblab4.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final UserService userService;

    @Autowired
    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    protected ResponseEntity<SuccessMessageDTO> registerUser(@RequestBody @NotNull @Valid UserCredentialsDTO userCredentialsDTO,
                                                             HttpServletRequest servletRequest) {
        String url = servletRequest
                .getRequestURL()
                .toString()
                .replace(servletRequest.getServletPath(), "");
        userService.registerUser(userCredentialsDTO, url);

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Registration successful! Please check your mail!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @PostMapping("/login")
    protected ResponseEntity<JwtResponseDTO> loginUser(@RequestBody @NotNull @Valid UserCredentialsDTO userCredentialsDTO) {
        JwtResponseDTO jwtResponseDTO = userService.loginUser(userCredentialsDTO);

        return ResponseEntity.ok().body(jwtResponseDTO);
    }

    @PostMapping("/logout")
    protected ResponseEntity<SuccessMessageDTO> logoutUser(@AuthenticationPrincipal CustomBearerUser customBearerUser) {
        userService.logoutUser(customBearerUser.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Logout successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @PostMapping("/refresh")
    protected ResponseEntity<JwtResponseDTO> refreshUser(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO) {

        JwtResponseDTO jwtResponseDTO = userService.refreshUser(refreshTokenDTO.getRefreshToken());

        return ResponseEntity.ok().body(jwtResponseDTO);
    }

    @GetMapping("/verify")
    protected ResponseEntity<VerifyMessageDTO> verifyUser(@RequestParam("verification_code") String verificationCode) {
        userService.verifyUser(verificationCode);

        VerifyMessageDTO verifyMessageDTO = new VerifyMessageDTO("Verification successful! You can login now!");
        return ResponseEntity.ok().body(verifyMessageDTO);
    }

}
