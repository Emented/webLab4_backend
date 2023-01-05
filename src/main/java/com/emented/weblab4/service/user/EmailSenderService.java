package com.emented.weblab4.service.user;

import com.emented.weblab4.DAO.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailSenderService {
    void sendVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException;
}
