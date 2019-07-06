package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.tdfparser.processor.StringProcessor;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringProcessorTest {
    private final StringProcessor stringProcessor = new StringProcessor();

    @Test
    public void normal() {
        assertEquals("abc", stringProcessor.process("\"abc\""));
        assertEquals("a\"bc\"", stringProcessor.process("\"a\\\"bc\\\"\""));
    }

    @Test
    public void not_string() {
        assertEquals("a\\\"bc\\\"", stringProcessor.process("a\\\"bc\\\""));
        assertEquals("abc", stringProcessor.process("abc"));
    }

    @Test
    public void empty_string() {
        assertEquals("", stringProcessor.process("\"\""));
    }

}
