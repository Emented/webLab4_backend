package com.emented.weblab4.service.user;


import com.emented.weblab4.DTO.JwtResponseDTO;
import com.emented.weblab4.DTO.UserCredentialsDTO;
import com.emented.weblab4.exception.*;
import com.emented.weblab4.model.User;
import com.emented.weblab4.repository.UserRepository;
import com.emented.weblab4.sequrity.service.JwtTokenUtil;
import com.emented.weblab4.sequrity.service.JwtTokenUtilImpl;
import com.emented.weblab4.sequrity.service.UserDetailsImpl;
import com.emented.weblab4.util.RandomKeyGen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private final EmailSenderService emailSenderService;

    @Value("${key.size}")
    private Integer keySize;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RandomKeyGen randomKeyGen,
                           AuthenticationManager authenticationManager,
                           JwtTokenUtilImpl jwtTokenUtil,
                           EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.randomKeyGen = randomKeyGen;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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
        user.setRoles(userCredentialsDTO.getRoles());

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentialsDTO.getEmail(),
                        userCredentialsDTO.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Integer userId = userDetails.getUserId();
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUserId());
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUserId());

        return new JwtResponseDTO(userId, accessToken, refreshToken);

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

        Integer userId = jwtTokenUtil.getUserIdFromRefreshToken(refreshToken);
        String accessToken = jwtTokenUtil.generateAccessToken(userId);

        return new JwtResponseDTO(userId, accessToken, refreshToken);
    }
}
