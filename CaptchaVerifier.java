package com.project.Bootcamp.util;

import org.springframework.stereotype.Component;

@Component
public class CaptchaVerifier {

    public boolean verify(String token) {
        // Simulated verification for demo purposes
        return "valid-captcha".equals(token);
    }
}
