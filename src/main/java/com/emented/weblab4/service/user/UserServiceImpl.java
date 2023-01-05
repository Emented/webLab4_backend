package com.emented.weblab4.service.user;


import com.emented.weblab4.DAO.User;
import com.emented.weblab4.DTO.JwtResponseDTO;
import com.emented.weblab4.DTO.UserCredentialsDTO;
import com.emented.weblab4.exception.*;
import com.emented.weblab4.repository.UserRepository;
import com.emented.weblab4.sequrity.service.JwtTokenUtil;
import com.emented.weblab4.sequrity.service.JwtTokenUtilImpl;
import com.emented.weblab4.sequrity.service.JwtUserDetailsService;
import com.emented.weblab4.util.RandomKeyGen;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomKeyGen randomKeyGen;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final EmailSenderService emailSenderService;

    @Value("${key.size}")
    private Integer keySize;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RandomKeyGen randomKeyGen,
                           AuthenticationManager authenticationManager,
                           JwtTokenUtilImpl jwtTokenUtil,
                           JwtUserDetailsService jwtUserDetailsService,
                           EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.randomKeyGen = randomKeyGen;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void registerUser(UserCredentialsDTO userCredentialsDTO, String url) {
        String randomCode = randomKeyGen.generateKey(keySize);

        User user = new User();
        user.setEmail(userCredentialsDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCredentialsDTO.getPassword()));
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        try {
            Integer id = userRepository.saveUser(user);
            user.setId(id);
        } catch (DataIntegrityViolationException e) {
            log.info("User with email: {} already exists!", user.getEmail());
            throw new UserAlreadyExistsException("User already exists!");
        }


        try {
            emailSenderService.sendVerificationEmail(user, url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            userRepository.deleteUserById(user.getId());
            log.error("Error during sending verification message to {}", user.getEmail());
            throw new VerificationMessageCannotBeSentException("Error during sending verification message!");
        }

    }

    @Override
    public JwtResponseDTO loginUser(UserCredentialsDTO userCredentialsDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentialsDTO.getEmail(),
                        userCredentialsDTO.getPassword()));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userCredentialsDTO.getEmail());

        String email = userDetails.getUsername();
        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);

        return new JwtResponseDTO(email, accessToken, refreshToken);

    }

    @Override
    public void logoutUser(Integer userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public void verifyUser(String verificationCode) {
        Optional<User> userOptional = userRepository.findUserByVerificationCode(verificationCode);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isEnabled()) {
                log.info("User with email: {} already verified!", user.getEmail());
                throw new UserAlreadyVerifiedException("User is already verified!");
            }
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.updateUser(user);
        } else {
            log.info("User doesn't exist!");
            throw new UserDoesNotExistException("User does not exist!");
        }
    }

    @Override
    public JwtResponseDTO refreshUser(String refreshToken) {

        if (!jwtTokenUtil.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token!");
        }

        String username = jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(username);

        return new JwtResponseDTO(username, accessToken, newRefreshToken);
    }
}
