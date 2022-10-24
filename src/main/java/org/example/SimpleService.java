package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleService {

    /**
     * Simple method prints log and standard console out message
     */
    public void action() {
        log.info("Log info print from service");
        log.debug("Log debug print from service");
        log.warn("Log warn print from service");
        log.error("Log error print from service");
        System.out.println("System print from service");
    }
}
