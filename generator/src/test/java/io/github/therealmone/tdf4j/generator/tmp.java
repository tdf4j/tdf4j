package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

public class tmp {
    @Test
    public void temp() {
        final Generator<Parser> generator = new ParserGenerator();
        final Parser parser = generator.generate(new AbstractParserModule() {
            @Override
            public void configure() {
            }
        });
        parser.parse("any");
    }
}
