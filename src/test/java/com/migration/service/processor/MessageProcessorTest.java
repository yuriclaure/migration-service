package com.migration.service.processor;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.migration.service.configuration.AwsProperties;
import com.migration.service.dto.DynamoDbRecord;
import com.migration.service.dto.MigrationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class MessageProcessorTest {
    private static final String QUEUE_URL = "https://queue";

    @Mock
    private AmazonSQSAsync sqs;
    @Mock
    private IDynamoDBMapper dynamoDbMapper;
    @Mock
    private AwsProperties awsProperties;

    private MessageProcessor messageProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(awsProperties.getQueueUrl()).thenReturn(QUEUE_URL);

        messageProcessor = new MessageProcessor(sqs, dynamoDbMapper, awsProperties);
    }

    @Test
    public void givenRecordToMigrate_whenCallingMessageProcessor_expectRecordSavedInDynamo() {
        MigrationRequest migrationRequest = new MigrationRequest("id", 1, 10.0, 5.0, 5.0);

        messageProcessor.accept(migrationRequest);

        verify(dynamoDbMapper).save(new DynamoDbRecord(migrationRequest));
    }

    @Test
    public void givenRecordToMigrate_whenCallingMessageProcessor_expectMessageDeletedFromSqs() {
        String messageId = "id";
        MigrationRequest migrationRequest = new MigrationRequest(messageId, 2, 10.0, 5.0, 5.0);

        messageProcessor.accept(migrationRequest);

        verify(sqs).deleteMessage(QUEUE_URL, messageId);
    }



}