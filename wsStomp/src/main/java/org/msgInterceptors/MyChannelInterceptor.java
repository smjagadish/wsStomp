package org.msgInterceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;

public class MyChannelInterceptor implements ExecutorChannelInterceptor {
    @Autowired
    private TaskExecutor ts;
    private Logger logger = LoggerFactory.getLogger(MyChannelInterceptor.class);

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // applies for both spring handled sends and relayed sends
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if(command == StompCommand.SEND) {
            String dest = (String) message.getHeaders().get("simpDestination");
            // only process if its a specific relayed send
            if (dest.contains("exchange"))
                logger.info("invoking interceptor for rmq exchange send request");
        }
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // invoked for both receipt from spring native broker and relayed broker
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command == StompCommand.MESSAGE){
            String dest = (String)  message.getHeaders().get("destination");
            // only process a specific relayed queue
            if (dest.contains("stomprcv"))
                logger.info("invoking interceptor for amq topic subscribe resp");
        }
        return ExecutorChannelInterceptor.super.postReceive(message, channel);
    }
}
