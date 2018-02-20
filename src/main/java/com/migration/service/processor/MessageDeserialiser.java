package com.migration.service.processor;

import com.amazonaws.services.sqs.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MessageDeserialiser implements Function<Message, MigrationRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDeserialiser.class);

    private final JsonDeserialiser jsonDeserialiser;

    @Autowired
    public MessageDeserialiser() {
        this(new JsonDeserialiser());
    }

    MessageDeserialiser(JsonDeserialiser jsonDeserialiser) {
        this.jsonDeserialiser = jsonDeserialiser;
    }

    @Override
    public MigrationRequest apply(Message sqsMessage) {
        try {
            MigrationRequest message = jsonDeserialiser.deserialise(sqsMessage.getBody());
            LOGGER.info("Message {} successfully transformed to {}", sqsMessage.getBody(), message.toString());
            return message;
        } catch (Exception e) {
            LOGGER.error("Failed to deserialise message " + sqsMessage.getBody(), e);
            throw new MigrationFailureException("Failed to deserialise message", e);
        }
    }

    /**
     * Used for testing as Gson is not mockable
     */
    static class JsonDeserialiser {
        private final Gson gson;

        JsonDeserialiser() {
            this.gson = new GsonBuilder().create();
        }

        MigrationRequest deserialise(String json) {
            return gson.fromJson(json, MigrationRequest.class);
        }
    }
}
