package pl.dk.aibron_first_task.twilio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.user.dtos.OnRegistrationEvent;

@Service
@Profile("test")
class TwilioServiceImpForTests implements TwilioService {
    private final Logger logger = LoggerFactory.getLogger(TwilioServiceImpForTests.class);

    @Override
    public void sendEventNotificationSMS() {
        logger.debug("Sending notification sms");
    }

    @Override
    @EventListener
    public void sendRegisterNotification(OnRegistrationEvent user) {
        logger.debug("Sending register notification");
        logger.debug("The registration notification has been sent");
    }
}
