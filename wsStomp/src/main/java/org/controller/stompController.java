package org.controller;

import org.clientModel.clientData;
import org.serverModel.serverData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class stompController {
    private static final Logger logger = LoggerFactory.getLogger(stompController.class);
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
}
