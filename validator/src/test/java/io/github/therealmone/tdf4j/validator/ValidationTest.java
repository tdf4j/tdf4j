package io.github.therealmone.tdf4j.validator;

import io.github.therealmone.tdf4j.commons.Module;

import static org.junit.Assert.*;

public class ValidationTest {

    protected <M extends Module> void assertThrows(final Validator<M> validator, final M module, final ValidatorException ex) {
        try {
            validator.validate(module);
            fail("Expected exception " + ex);
        } catch (ValidatorException e) {
            assertEquals(ex, e);
        }
    }

}
