package com.migration.service.processor;

import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class MessageValidator implements Predicate<MigrationRequest> {

    @Override
    public boolean test(MigrationRequest migrationRequest) {
        if (!migrationRequest.getTotal().equals(migrationRequest.getValor() - migrationRequest.getDesconto())) {
            throw new MigrationFailureException("Message has invalid values");
        }
        return true;
    }
}
