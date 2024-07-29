package pl.dk.aibron_first_task.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dk.aibron_first_task.websocket.dtos.Message;
import pl.dk.aibron_first_task.websocket.dtos.OutputMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return new OutputMessage(message.name(), message.message(), time);
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
