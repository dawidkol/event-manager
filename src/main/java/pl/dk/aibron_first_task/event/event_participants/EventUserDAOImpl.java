package pl.dk.aibron_first_task.event.event_participants;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
class EventUserDAOImpl implements EventUserDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EventUserRowMapper eventUserRowMapper;

    @Override
    public List<EventUser> findAllEventParticipants(Long eventId) {
        return jdbcTemplate.query("SELECT * FROM event_user WHERE event_id = ?", eventUserRowMapper);
    }

    @Override
    public void saveParticipant(EventUser eventUser) {
        String sql = "INSERT INTO event_user (event_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, eventUser.eventId(), eventUser.userId());
    }

}
