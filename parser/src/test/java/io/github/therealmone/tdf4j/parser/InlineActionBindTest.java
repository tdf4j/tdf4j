package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

import static org.junit.Assert.*;

public class InlineActionBindTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline("System.out.println(\"inline action\")")
                        );
            }
        }.build();
        assertEquals("System.out.println(\"inline action\")", module.getGrammar().productions().get(0).elements().get(0).asInlineAction().code());
        assertEquals("prod1 := <<\nSystem.out.println(\"inline action\")\n>>", module.getGrammar().productions().get(0).toString());
    }

    @Test(expected = IllegalStateException.class)
    public void null_code() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline(null)
                        );
            }
        }.build();
    }

    @Test(expected = IllegalStateException.class)
    public void blank_code() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline("     ")
                        );
            }
        }.build();
    }
}
