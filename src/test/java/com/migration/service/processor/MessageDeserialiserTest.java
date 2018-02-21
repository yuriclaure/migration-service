package com.migration.service.processor;

import com.amazonaws.services.sqs.model.Message;
import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class MessageDeserialiserTest {
    private static final String WELL_FORMED_MESSAGE = "2,10.0,5.0,5.0";
    private static final String MALFORMED_MESSAGE = "3,10.0,5.0";
    private static final String MESSAGE_ID = "messageId";

    @Mock
    private Message sqsMessage;

    private MessageDeserialiser messageDeserialiser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(sqsMessage.getBody()).thenReturn(WELL_FORMED_MESSAGE);
        when(sqsMessage.getReceiptHandle()).thenReturn(MESSAGE_ID);

        messageDeserialiser = new MessageDeserialiser();
    }

    @Test
    public void givenWellformedMessage_whenDeserialising_expectMigrationRequest() {
        MigrationRequest expectedRequest = new MigrationRequest(MESSAGE_ID, WELL_FORMED_MESSAGE.split(","));

        MigrationRequest migrationRequest = messageDeserialiser.apply(sqsMessage);

        assertThat(migrationRequest, is(notNullValue()));
        assertThat(migrationRequest.getMessageId(), is(expectedRequest.getMessageId()));
        assertThat(migrationRequest.getCobrancaId(), is(expectedRequest.getCobrancaId()));
        assertThat(migrationRequest.getValor(), is(expectedRequest.getValor()));
        assertThat(migrationRequest.getDesconto(), is(expectedRequest.getDesconto()));
        assertThat(migrationRequest.getTotal(), is(expectedRequest.getTotal()));
    }

    @Test(expected = MigrationFailureException.class)
    public void givenMalformedMessage_whenDeserialising_expectException() {
        when(sqsMessage.getBody()).thenReturn(MALFORMED_MESSAGE);

        messageDeserialiser.apply(sqsMessage);
    }

}