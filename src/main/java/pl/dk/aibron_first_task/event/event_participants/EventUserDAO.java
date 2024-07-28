package pl.dk.aibron_first_task.event.event_participants;

import java.util.List;

public interface EventUserDAO {

    List<EventUser> findAllEventParticipants(Long eventId);

    void saveParticipant(EventUser eventUser);
}
