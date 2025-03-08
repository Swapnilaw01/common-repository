package com.example.workflow;

import jakarta.annotation.PostConstruct;
import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonCamundaWorker {
    private static final Logger logger = LoggerFactory.getLogger(CommonCamundaWorker.class);
    private final ExternalTaskClient client;
    private final List<String> topics;
    private final GenericExternalTaskHandler taskHandler;

    public CommonCamundaWorker(String camundaUrl, List<String> topics,
                               GenericExternalTaskHandler taskHandler) {
        System.out.println("Common Repos CommonCamundaWorker topics list Pre: " + topics);
        this.topics = topics;
        this.taskHandler = taskHandler;
        this.client = ExternalTaskClient.create()
                .baseUrl(camundaUrl)
                .asyncResponseTimeout(10000)  // Polling interval
                .lockDuration(5000)  // Locking duration for tasks
                .build();

        System.out.println("Common Repos CommonCamundaWorker topics list Post: " + topics);
    }

    @PostConstruct
    public void subscribeToTopics() {
        for (String topic : topics) {
            client.subscribe(topic)
                    .lockDuration(5000)
                    .handler(taskHandler)
                    .open();
            System.out.println("Worker subscribed to topic:: " + topic);
            logger.info("Worker subscribed to topic: {}", topic);
        }
    }
}
