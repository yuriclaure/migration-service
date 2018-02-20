package com.migration.service.processor;

import com.migration.service.dto.MigrationRequest;
import com.migration.service.exceptions.MigrationFailureException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Before;
import org.junit.Test;

public class MessageValidatorTest {

    private MessageValidator messageValidator;

    @Before
    public void setUp() {
        messageValidator = new MessageValidator();
    }

    @Test
    public void givenValidMessage_whenValidating_expectTrueReturned() {
        MigrationRequest validMessage = new MigrationRequest("id", "cobrancaId", 10.0, 5.0, 5.0);

        boolean result = messageValidator.test(validMessage);

        assertThat(result, is(true));
    }

    @Test(expected = MigrationFailureException.class)
    public void givenInvalidMessage_whenValidating_expectExceptionThrown() {
        MigrationRequest invalidMessage = new MigrationRequest("id", "cobrancaId", 10.0, 5.0, 10.0);

        messageValidator.test(invalidMessage);
    }

}