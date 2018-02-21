package com.migration.service.processor;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.migration.service.configuration.AwsProperties;
import com.migration.service.dto.DynamoDbRecord;
import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MessageProcessor implements Consumer<MigrationRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageValidator.class);

    private final AmazonSQSAsync sqs;
    private final IDynamoDBMapper dynamoDBMapper;
    private final AwsProperties awsProperties;

    @Autowired
    public MessageProcessor(AmazonSQSAsync sqs, IDynamoDBMapper dynamoDBMapper, AwsProperties awsProperties) {
        this.sqs = sqs;
        this.dynamoDBMapper = dynamoDBMapper;
        this.awsProperties = awsProperties;
    }

    @Override
    public void accept(MigrationRequest migrationRequest) {
        try {
            dynamoDBMapper.save(new DynamoDbRecord(migrationRequest));
            sqs.deleteMessage(awsProperties.getQueueUrl(), migrationRequest.getMessageId());
            LOGGER.info("({}) - Message successfully migrated to DynamoDB", migrationRequest.getCobrancaId());
        } catch (Exception e) {
            LOGGER.error("(" + migrationRequest.getCobrancaId() + ") - Error while saving in DynamoDB", e);
            throw new MigrationFailureException("Failed to save to dynamoDB", e);
        }
    }
}
