package org.dispatcher;


import org.serverModel.serverData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.userService.userAdd;

import java.util.Iterator;

@Configuration
public class taskDispatch {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private userAdd userUtil;
    @Scheduled(fixedRate = 1000 ,initialDelay = 25000)
    // broadcasts every second to all clients
    @Profile("!dry")
    public void dispatch()
    {
        serverData outData = new serverData();
        outData.setConfirmationID(1);
        outData.setDescription("out of bound quote");
        outData.setRemarks("check back tomorrow");
        template.convertAndSend("/topic/status",outData);
    }

    // scheduled send to a specific session
    // maybe in later spring version i don't need to do the call tio createHeaders()
    // currently if i dont include, the msg is not sent to correct session
    // i store session id's and access them for sending. ugly because i don't remove from hashset when sessions disconnect
    // enrich by having session connect/disconnect listeners
    @Scheduled(fixedRate = 3003 ,initialDelay = 25000)
    @Profile("!dry")
    public void unicast()
    {
        serverData outData = new serverData();
        outData.setConfirmationID(98);
        outData.setRemarks("scheduled unicast");
        outData.setDescription("mock unicast");
        Iterator<String> sessionIter = userUtil.getSessions().iterator();
        while(sessionIter.hasNext())
        {
            String id = sessionIter.next();

            template.convertAndSendToUser(id,"/queue/status",outData,createHeaders(id));
        }
    }
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();

    }

}
