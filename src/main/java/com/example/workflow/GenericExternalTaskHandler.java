package com.example.workflow;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.Map;
import java.util.function.BiConsumer;

public class GenericExternalTaskHandler implements ExternalTaskHandler {
    private final Map<String, BiConsumer<ExternalTask, ExternalTaskService>> topicHandlers;

    public GenericExternalTaskHandler(Map<String, BiConsumer<ExternalTask, ExternalTaskService>> topicHandlers) {
        this.topicHandlers = topicHandlers;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String topic = externalTask.getTopicName();
        if (topicHandlers.containsKey(topic)) {
            System.out.println("Worker GenericExternalTaskHandler to topic:: " + topic);
            topicHandlers.get(topic).accept(externalTask,externalTaskService);
        } else {
            System.out.println("Worker GenericExternalTaskHandler Complete called for:: " + topic);
            externalTaskService.complete(externalTask); // Default handling if no handler for the topic
        }
    }
}
