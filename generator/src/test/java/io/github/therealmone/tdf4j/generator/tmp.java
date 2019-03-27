package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.LexerFactory;
import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

public class tmp {
    @Test
    public void temp() {
        final Generator<Parser> generator = new ParserGenerator();
        final Lexer lexer = new LexerFactory().withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("lex1").pattern("lex1");
                tokenize("lex2").pattern("lex2");
                tokenize("lex3").pattern("lex3");
            }
        });
        long current = System.currentTimeMillis();
        final Parser parser = generator.generate(new AbstractParserModule() {
            @Override
            public void configure() {
            }
        });
        System.out.println("Compilation time: " + (System.currentTimeMillis() - current));
        current = System.currentTimeMillis();
        parser.parse(lexer.stream("lex3 lex2 lex1"));
        parser.parse(lexer.analyze("lex1 lex2 lex3"));
        System.out.println("Parsing time: " + (System.currentTimeMillis() - current));
    }
}
