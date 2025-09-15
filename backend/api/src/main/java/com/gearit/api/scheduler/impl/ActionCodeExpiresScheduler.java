package com.gearit.api.scheduler.impl;

import com.gearit.api.scheduler.JobScheduler;
import com.gearit.api.service.actioncode.ActionCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActionCodeExpiresScheduler implements JobScheduler {

    private final ActionCodeService actionCodeService;

    private final Logger logger = LoggerFactory.getLogger(ActionCodeExpiresScheduler.class);

    @Autowired
    public ActionCodeExpiresScheduler(ActionCodeService actionCodeService) {
        this.actionCodeService = actionCodeService;
    }

    /**
     * Периодическое удаление истёкших отправленных кодов, каждые 12 часов.
     */
    @Override
    @Scheduled(fixedRate = 43200000)
    public void schedule() {
        try {
            logger.info("Start delete expires action codes");

            int count = actionCodeService.deleteExpiresActionCodes();

            logger.info("End delete expires action codes, count removed: {}", count);
        } catch (Exception e) {
            logger.error("Error while delete expires action codes: {}", e.getMessage());
        }
    }
}
