package pl.dk.aibron_first_task.twilio;

import pl.dk.aibron_first_task.user.dtos.OnRegistrationEvent;

public interface TwilioService {

    void sendEventNotificationSMS();
    void sendRegisterNotification(OnRegistrationEvent user);
}
