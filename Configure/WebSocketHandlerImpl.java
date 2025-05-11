package com.mb.Configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandlerImpl  extends TextWebSocketHandler 
{
 
	 

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
         try {
        	 session.sendMessage(new TextMessage("Message received :"+message.getPayload()));
         }
         catch(Exception e) {
        	 e.printStackTrace();
         }
    }
}
