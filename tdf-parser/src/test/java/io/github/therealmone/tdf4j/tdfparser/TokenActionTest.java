package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.LexerOptions;
import io.github.therealmone.tdf4j.generator.ParserOptions;
import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokenActionTest extends TdfParserTest {
    public static List<String> tokens = new ArrayList<>();

    @Test
    public void test() {
        final Interpreter interpreter = generate("TokenActionTest.tdf");
        final Lexer lexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(interpreter.getLexerModule())
                .build()
        ).generate();
        final Parser parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("io.github.therealmone.tdf4j.tdfparser")
                .setClassName("TokenActionTest_parser")
                .setModule(interpreter.getParserModule())
                .build()
        ).generate();
        assertNotNull(parser.parse(lexer.stream("ABC")));
        assertEquals(2, tokens.size());
        assertEquals("A", tokens.get(0));
        assertEquals("C", tokens.get(1));
    }

}
