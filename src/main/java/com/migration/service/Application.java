package com.migration.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.migration.service.configuration.AwsProperties;
import com.migration.service.processor.MessageDeserialiser;
import com.migration.service.processor.MessageProcessor;
import com.migration.service.processor.MessageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private final AmazonSQSAsync sqs;
    private final AwsProperties awsProperties;
    private final MessageDeserialiser messageDeserialiser;
    private final MessageValidator messageValidator;
    private final MessageProcessor messageProcessor;

    @Autowired
    public Application(AmazonSQSAsync sqs,
                       AwsProperties awsProperties,
                       MessageDeserialiser messageDeserialiser,
                       MessageValidator messageValidator,
                       MessageProcessor messageProcessor) {
        this.sqs = sqs;
        this.awsProperties = awsProperties;
        this.messageDeserialiser = messageDeserialiser;
        this.messageValidator = messageValidator;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void run(String... args) {
        do {
            try {
                getMessages().parallelStream().map(messageDeserialiser).filter(messageValidator).forEach(messageProcessor);
            } catch (Exception e) {
                // ignore exception and retry
            }
        } while (true);
    }

    private List<Message> getMessages() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(awsProperties.getQueueUrl()).withMaxNumberOfMessages(10).withWaitTimeSeconds(5);
        return sqs.receiveMessage(receiveMessageRequest).getMessages();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

}
