package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractParserModuleTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").then("ele1").then("ele2");
                prod("prod2").then("ele1", "ele2");
            }
        }.build();
        assertEquals(2, module.getProductions().size());
        {
            assertEquals("prod2", module.getProductions().get(0).identifier());
            assertEquals(2, module.getProductions().get(0).elements().size());
            assertEquals("ele1", module.getProductions().get(0).elements().get(0));
            assertEquals("ele2", module.getProductions().get(0).elements().get(1));
        }
        {
            assertEquals("prod1", module.getProductions().get(1).identifier());
            assertEquals(2, module.getProductions().get(1).elements().size());
            assertEquals("ele1", module.getProductions().get(1).elements().get(0));
            assertEquals("ele2", module.getProductions().get(1).elements().get(1));
        }
    }

    @Test
    public void no_prods() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
            }
        }.build();
        assertEquals(0, module.getProductions().size());
    }

    @Test(expected = RuntimeException.class)
    public void null_ident() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod(null);
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void blank_ident() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("   ");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void unique_identifiers() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1");
                prod("prod1");
            }
        }.build();
    }
}
