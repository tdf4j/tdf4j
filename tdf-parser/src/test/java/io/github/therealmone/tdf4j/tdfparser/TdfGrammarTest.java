package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.LexerGenerator;
import io.github.therealmone.tdf4j.generator.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import org.junit.Before;
import org.junit.Ignore;

public class TdfGrammarTest extends FullGrammarTest {

    @Override
    @Before
    public void before() {
        System.out.println(tdfParser.parse(tdfLexer.stream(load("TdfGrammar.tdf"))));
        System.out.println(tdfParser.getLexerModule().build().getTerminals());
        System.out.println(tdfParser.getParserModule().build().getGrammar());

        final TdfParser tempParser = ParserGenerator.newInstance().generate(tdfParser.getParserModule().build(), TdfParser.class);
        final Lexer tempLexer = LexerGenerator.newInstance().generate(tdfParser.getLexerModule().build());
        System.out.println(tempParser.parse(tempLexer.stream(load("FullGrammarTest.tdf"))));
        System.out.println(tempParser.getLexerModule().build().getTerminals());
        System.out.println(tempParser.getParserModule().build().getGrammar());

        this.parser = ParserGenerator.newInstance().generate(tempParser.getParserModule().build());
        this.lexer = LexerGenerator.newInstance().generate(tempParser.getLexerModule().build());
    }
}
