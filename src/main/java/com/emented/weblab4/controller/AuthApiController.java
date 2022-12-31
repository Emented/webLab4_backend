package com.emented.weblab4.controller;

import com.emented.weblab4.DTO.JwtResponseDTO;
import com.emented.weblab4.DTO.SuccessMessageDTO;
import com.emented.weblab4.DTO.UserCredentialsDTO;
import com.emented.weblab4.DTO.VerifyMessageDTO;
import com.emented.weblab4.sequrity.service.UserDetailsImpl;
import com.emented.weblab4.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final UserService userService;

    @Autowired
    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<SuccessMessageDTO> registerUser(@RequestBody @NotNull @Valid UserCredentialsDTO userCredentialsDTO,
                                                           HttpServletRequest servletRequest) {
        String url = servletRequest
                .getRequestURL()
                .toString()
                .replace(servletRequest.getServletPath(), "");
        userService.registerUser(userCredentialsDTO, url);

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Registration successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @PostMapping("/login")
    private ResponseEntity<JwtResponseDTO> loginUser(@RequestBody @NotNull @Valid UserCredentialsDTO userCredentialsDTO) {
        String key = userService.loginUser(userCredentialsDTO);
        return ResponseEntity.ok().body(new JwtResponseDTO(key));
    }

    @PostMapping("/logout")
    private ResponseEntity<SuccessMessageDTO> logoutUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logoutUser(userDetails.getUserId());

        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("Logout successful!");
        return ResponseEntity.ok().body(successMessageDTO);
    }

    @GetMapping("/verify")
    private ResponseEntity<VerifyMessageDTO> verifyUser(@RequestParam("verification_code") String verificationCode) {
        userService.verifyUser(verificationCode);
        VerifyMessageDTO verifyMessageDTO = new VerifyMessageDTO("Verification successful!");
        return ResponseEntity.ok().body(verifyMessageDTO);
    }

}
