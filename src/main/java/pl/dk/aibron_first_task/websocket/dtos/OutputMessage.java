package pl.dk.aibron_first_task.websocket.dtos;

import lombok.Builder;

@Builder
public record OutputMessage(String from, String message, String date) {
}
