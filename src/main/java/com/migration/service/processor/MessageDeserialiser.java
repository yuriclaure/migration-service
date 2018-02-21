package com.migration.service.processor;

import com.amazonaws.services.sqs.model.Message;
import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MessageDeserialiser implements Function<Message, MigrationRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDeserialiser.class);

    @Override
    public MigrationRequest apply(Message sqsMessage) {
        String body = sqsMessage.getBody();
        try {
            MigrationRequest message = new MigrationRequest(sqsMessage.getReceiptHandle(), body.split(","));
            LOGGER.info("({}) - Message {} successfully transformed to {}", message.getCobrancaId(), body, message.toString());
            return message;
        } catch (Exception e) {
            LOGGER.error("(-) - Failed to deserialise message " + body, e);
            throw new MigrationFailureException("Failed to deserialise message", e);
        }
    }
}
