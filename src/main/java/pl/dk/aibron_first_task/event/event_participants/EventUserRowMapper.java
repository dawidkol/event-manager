package pl.dk.aibron_first_task.event.event_participants;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
class EventUserRowMapper implements RowMapper<EventUser> {
    @Override
    public EventUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        long rsEventId = rs.getLong(1);
        long rsUserId = rs.getLong(2);
        return new EventUser(rsEventId, rsUserId);
    }
}
