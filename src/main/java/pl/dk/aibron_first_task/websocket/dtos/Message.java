package pl.dk.aibron_first_task.websocket.dtos;

import lombok.Builder;

@Builder
public record Message(String name, String message){
}
