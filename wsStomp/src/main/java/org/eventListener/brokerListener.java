package org.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;

@Component
public class brokerListener implements ApplicationListener<BrokerAvailabilityEvent> {
    private Logger logger = LoggerFactory.getLogger(brokerListener.class);
    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        logger.info("received the event for broker availability:"+ event.isBrokerAvailable());

    }
}
