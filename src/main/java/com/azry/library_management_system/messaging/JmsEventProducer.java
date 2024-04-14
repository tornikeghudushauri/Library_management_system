package com.azry.library_management_system.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsEventProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${jms.queue.borrow}")
    private String borrowQueue;

    @Value("${jms.queue.return}")
    private String returnQueue;

    @Autowired
    public JmsEventProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendBorrowEvent(BookBorrowEvent event) {
            jmsTemplate.convertAndSend(borrowQueue, event);
    }

    public void sendReturnEvent(BookReturnEvent event) {
        jmsTemplate.convertAndSend(returnQueue, event);
    }
}