package com.jks.startup.springbootasync;

import com.jks.startup.springbootasync.model.Topic;
import com.jks.startup.springbootasync.service.LookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final LookupService lookupService;

    public AppRunner(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<Topic> page1 = lookupService.findUser("PivotalSoftware");
        CompletableFuture<Topic> page2 = lookupService.findUser("CloudFoundry");
        CompletableFuture<Topic> page3 = lookupService.findUser("Spring-Projects");

        // Wait until they are all done
        //CompletableFuture.allOf(page1,page2,page3).join();
        CompletableFuture.anyOf(page1,page2,page3);

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());

    }

}