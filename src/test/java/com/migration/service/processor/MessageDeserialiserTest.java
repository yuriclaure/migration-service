package com.migration.service.processor;

import com.amazonaws.services.sqs.model.Message;
import com.google.gson.JsonSyntaxException;
import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import com.migration.service.processor.MessageDeserialiser.JsonDeserialiser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class MessageDeserialiserTest {
    private static final String WELL_FORMED_MESSAGE = "wellformed";
    private static final String MALFORMED_MESSAGE = "malformed";

    private MessageDeserialiser messageDeserialiser;

    @Mock
    private JsonDeserialiser jsonDeserialiser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        messageDeserialiser = new MessageDeserialiser(jsonDeserialiser);
    }

    @Test
    public void givenWellformedMessage_whenDeserialising_expectMigrationRequest() {
        Message sqsMessage = mock(Message.class);
        MigrationRequest expectedRequest = new MigrationRequest("id", "cobrancaId", 10.0, 5.0, 5.0);
        when(sqsMessage.getBody()).thenReturn(WELL_FORMED_MESSAGE);
        when(jsonDeserialiser.deserialise(WELL_FORMED_MESSAGE)).thenReturn(expectedRequest);

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
        Message sqsMessage = mock(Message.class);
        when(sqsMessage.getBody()).thenReturn(MALFORMED_MESSAGE);
        when(jsonDeserialiser.deserialise(MALFORMED_MESSAGE)).thenThrow(new JsonSyntaxException(""));

        messageDeserialiser.apply(sqsMessage);
    }

}