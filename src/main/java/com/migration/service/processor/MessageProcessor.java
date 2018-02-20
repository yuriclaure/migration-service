package com.migration.service.processor;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.migration.service.configuration.AwsProperties;
import com.migration.service.dto.MigrationRequest;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MessageProcessor implements Consumer<MigrationRequest> {

    private final AmazonSQSAsync sqs;
    private final AwsProperties awsProperties;

    public MessageProcessor(AmazonSQSAsync sqs, AwsProperties awsProperties) {
        this.sqs = sqs;
        this.awsProperties = awsProperties;
    }

    @Override
    public void accept(MigrationRequest migrationRequest) {
        // TODO: write into dynamo
        sqs.deleteMessageAsync(awsProperties.getQueueUrl(), migrationRequest.getMessageId());
    }
}
