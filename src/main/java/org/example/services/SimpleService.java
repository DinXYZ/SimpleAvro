package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleService {

    public void action() {
        log.info("Log info print from service");
        log.debug("Log debug print from service");
        log.warn("Log warn print from service");
        log.error("Log error print from service");
    }
}
