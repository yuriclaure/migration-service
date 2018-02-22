package com.migration.service.processor;

import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class MessageValidator implements Predicate<MigrationRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageValidator.class);

    @Override
    public boolean test(MigrationRequest migrationRequest) {
        if (!migrationRequest.getTotal().equals(migrationRequest.getValor() - migrationRequest.getDesconto())) {
            LOGGER.error("({}) - Failed to validate message values ({} - {} != {})",
                         migrationRequest.getCobrancaId(),
                         migrationRequest.getValor(),
                         migrationRequest.getDesconto(),
                         migrationRequest.getTotal());
            throw new MigrationFailureException("Message has invalid values");
        }
        LOGGER.info("({}) - Message has correct values ({} - {} = {})",
                    migrationRequest.getCobrancaId(),
                    migrationRequest.getValor(),
                    migrationRequest.getDesconto(),
                    migrationRequest.getTotal());
        return true;
    }
}
