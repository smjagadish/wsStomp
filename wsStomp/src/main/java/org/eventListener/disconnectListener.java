package org.eventListener;

import org.sessionUtil.sessionCleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class disconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    sessionCleanup sessionHandler;
    private Logger logger = LoggerFactory.getLogger(disconnectListener.class);
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        logger.info("disconnect recieved for:"+event.getSessionId());
        sessionHandler.removeSession(event.getSessionId());

    }
}
