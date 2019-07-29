package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.LexerOptions;
import io.github.therealmone.tdf4j.generator.ParserOptions;
import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import org.junit.Before;

public class TdfGrammarTest extends FullGrammarTest {

    @Override
    @Before
    public void before() {
        final Interpreter interpreter = generate("TdfGrammar.tdf");
        System.out.println(interpreter.getLexerModule().build().getTerminals());
        System.out.println(interpreter.getParserModule().build().getGrammar());

        final TdfParser tempParser = (TdfParser) new ParserGenerator(new ParserOptions.Builder()
                .setPackage("io.github.therealmone.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_tempParser")
                .setModule(interpreter.getParserModule())
                .setInterface(TdfParser.class)
                .build()
        ).generate();
        final Lexer tempLexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(interpreter.getLexerModule())
                .build()
        ).generate();
        System.out.println(tempParser.parse(tempLexer.stream(load("FullGrammarTest.tdf"))));
        System.out.println(tempParser.getLexerModule().build().getTerminals());
        System.out.println(tempParser.getParserModule().build().getGrammar());

        this.parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("io.github.therealmone.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_parser")
                .setModule(tempParser.getParserModule())
                .build()
        ).generate();
        this.lexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(tempParser.getLexerModule())
                .build()
        ).generate();
    }
}
