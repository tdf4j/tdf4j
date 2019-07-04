package io.github.therealmone.tdf4j.tdfparser;

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
        final TdfParser tdf = generate("TokenActionTest.tdf");
        final Lexer lexer = new LexerGenerator(tdf.getLexerModule()).generate();
        final Parser parser = new ParserGenerator(tdf.getParserModule()).generate();
        assertNotNull(parser.parse(lexer.stream("ABC")));
        assertEquals(2, tokens.size());
        assertEquals("A", tokens.get(0));
        assertEquals("C", tokens.get(1));
    }

}
