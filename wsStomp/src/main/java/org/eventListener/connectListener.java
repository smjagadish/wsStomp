package org.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class connectListener implements ApplicationListener<SessionConnectEvent> {
    private Logger logger = LoggerFactory.getLogger(connectListener.class);
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        // doesn't invoke for system sessions, just client sessions
        logger.info("connect listener fired for:"+ event.getMessage().getHeaders().get("simpSessionId"));

    }
}
