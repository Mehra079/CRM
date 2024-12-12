package com.example.authentication.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    public SmsServiceImpl() {
        // Initialize Twilio with credentials
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void sendSms(String mobile, String message) {
        try {
            Message sms = Message.creator(
                    new com.twilio.type.PhoneNumber(mobile),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    message
            ).create();

            logger.info("SMS sent to {}: Message SID {}", mobile, sms.getSid());
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", mobile, e.getMessage());
        }
    }
}
