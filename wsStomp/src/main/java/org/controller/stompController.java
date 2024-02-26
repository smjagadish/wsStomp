package org.controller;

import org.clientModel.clientData;
import org.serverModel.serverData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.userService.userAdd;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@Controller
public class stompController {
    private static final Logger logger = LoggerFactory.getLogger(stompController.class);

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private userAdd userUtil;

    // recv msg on /app/quote
    // broadcast msg on /topic/status
    @MessageMapping("/quote")
    @SendTo("/topic/status")
    public serverData handleQuote(clientData data) throws InterruptedException {
        logger.info("quote recieved with following detail");
        logger.info("client ID:"+ data.getClientID());
        logger.info("client code:"+ data.getClientCode());
        logger.info("client model:"+ data.getModel());
        Thread.sleep(1000);
        serverData outData = new serverData();
        outData.setConfirmationID(100);
        outData.setDescription("quote has been accepted for model x");
        outData.setRemarks("check back in a week");
        return outData;

    }

    //recv message on /app/message
    //unicast msg on /user/<sessionID or user principal>/queue/status
    @MessageMapping("/message")
    @SendToUser("/queue/status")
    // inject the session ID for out of bounds transmission from server to client
    public serverData handleQueue(clientData data , @Header("simpSessionId") String sessionId, SimpMessageHeaderAccessor headers) throws InterruptedException {
        logger.info("user input recieved with following detail");
        logger.info("client ID:"+ data.getClientID());
        logger.info("client code:"+ data.getClientCode());
        logger.info("client model:"+ data.getModel());
        Thread.sleep(1000);
        serverData outData = new serverData();
        outData.setConfirmationID(100);
        outData.setDescription("user input has been accepted for model x");
        outData.setRemarks("check back in a week");
        logger.info("sessionID is: "+sessionId);
        userUtil.addSession(sessionId);
        return outData;

    }

    // this is an example of an async processing of the message send back to a client
    // what gets send in the scope of the message mapping controller is just an acknowledge/dummy message
    // the callback for the future , when it completes, does the sending of actual data that client may be interested in
    @MessageMapping("/async")
    @SendTo("/topic/ack")
    public String handleAsync(clientData data)
    {
        CompletableFuture<serverData> outData = new CompletableFuture<>();
        // this is just a sample, in reality future can be completed at a later stage
        outData.complete(serverData.builder()
                .confirmationID(1256)
                .remarks("processing complete")
                .description("order closed")
                .build());
        outData.thenAccept(res->this.template.convertAndSend("/topic/outasync",res));
        return "lazy ack, please check outasync broadcast result";
    }
    // subscriber mapping is a unique type of msg paradigm that skips the broker
    // think of it as a one time response to a subscription from client
    // since broker is not involved, further comm on this channel is not possible
    // for below code , client subs to /app/data and we return a response immediately
    @SubscribeMapping("/data")
    public String handlesub()
    {
        logger.info("rcvd");
        return "fire and forget";
    }
    @MessageExceptionHandler
    public void dumpLog()
    {
        logger.info("can't deliver the message");
    }
}
