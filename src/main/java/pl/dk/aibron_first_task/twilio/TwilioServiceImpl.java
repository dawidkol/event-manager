package pl.dk.aibron_first_task.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.event.Event;
import pl.dk.aibron_first_task.event.EventRepository;
import pl.dk.aibron_first_task.user.dtos.OnRegistrationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Profile("dev")
class TwilioServiceImpl implements TwilioService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone.number.from}")
    private String phoneNumberFrom;

    private final EventRepository eventRepository;

    @Override
    @Async
    @Scheduled(cron = "${scheduler.notification.sms}")
    public void sendEventNotificationSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        eventRepository.findEventsForNotification()
                .forEach(
                        event ->
                                event.getParticipants()
                                        .forEach(pN ->
                                                this.sendTwilioNotification(pN.getPhoneNumber(), this.createEventNotificationMessage(event, pN.getFirstName()))
                                        )
                );
    }

    private String createEventNotificationMessage(Event event, String userFirstName) {
        String name = event.getName();
        LocalDateTime eventStart = event.getEventStart();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedEventStart = eventStart.format(formatter);
        return """
                Hello %s!
                Just a reminder that tomorrow event: %s starts at %s
                """.formatted(userFirstName, name, formattedEventStart);
    }

    @Async
    public void sendTwilioNotification(String phoneNumberTo, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(phoneNumberTo),
                new PhoneNumber(phoneNumberFrom),
                (message)).create();
    }

    @Override
    @EventListener
    @Async
    public void sendRegisterNotification(OnRegistrationEvent user) {
        String message = this.createRegistrationMessage(user.firstName());
        sendTwilioNotification(user.phoneNumber(), message);
    }

    private String createRegistrationMessage(String firstName) {
        return """
                Hello %s!
                Thank you for registering to our service.
                """.formatted(firstName);
    }
}
