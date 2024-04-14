package com.azry.library_management_system.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JmsEventListener {

    @JmsListener(destination = "${jms.queue.borrow}")
    public void processBorrowEvent(BookBorrowEvent event) {
        log.info("Book with id: {} borrowed by user with id: {}", event.getBookId(), event.getUserId());
    }

    @JmsListener(destination = "${jms.queue.return}")
    public void processReturnEvent(BookReturnEvent event) {
        log.info("Book with id: {} returned by user with id: {}", event.getBookId(), event.getUserId());
    }
}