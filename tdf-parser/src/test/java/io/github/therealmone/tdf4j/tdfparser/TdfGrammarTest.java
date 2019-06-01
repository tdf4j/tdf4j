package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import org.junit.Before;

public class TdfGrammarTest extends FullGrammarTest {

    @Override
    @Before
    public void before() {
        System.out.println(tdfParser.parse(tdfLexer.stream(load("TdfGrammar.tdf"))));
        System.out.println(tdfParser.getLexerModule().build().getTerminals());
        System.out.println(tdfParser.getParserModule().build().getGrammar());

        final TdfParser tempParser = new ParserGenerator(tdfParser.getParserModule().build()).generate(TdfParser.class);
        final Lexer tempLexer = new LexerGenerator(tdfParser.getLexerModule().build()).generate();
        System.out.println(tempParser.parse(tempLexer.stream(load("FullGrammarTest.tdf"))));
        System.out.println(tempParser.getLexerModule().build().getTerminals());
        System.out.println(tempParser.getParserModule().build().getGrammar());

        this.parser = new ParserGenerator(tempParser.getParserModule().build()).generate();
        this.lexer = new LexerGenerator(tempParser.getLexerModule().build()).generate();
    }
}
