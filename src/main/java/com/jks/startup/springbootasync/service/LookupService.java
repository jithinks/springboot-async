package com.jks.startup.springbootasync.service;

import com.jks.startup.springbootasync.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

import static com.jks.startup.springbootasync.util.ExternalAppAuth.getHeader;

@Service
public class LookupService {

    private static final Logger logger = LoggerFactory.getLogger(LookupService.class);

    private final RestTemplate restTemplate;

    public LookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    @Async
    public CompletableFuture<Topic> findUser(String login) throws InterruptedException {
        logger.info("Looking up " + login);

       /* String url = String.format("https://api.github.com/users/%s", login);
        Topic results = restTemplate.getForObject(url, Topic.class);*/

        String url = String.format("http://localhost:1234/startup/topic/%s", login);
        HttpHeaders headers = getHeader();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Topic> response = restTemplate.exchange(url, HttpMethod.GET, request, Topic.class);
        Topic topic = response.getBody();

        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(topic);
    }


}
