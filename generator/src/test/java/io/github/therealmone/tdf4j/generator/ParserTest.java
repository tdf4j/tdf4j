package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.UnexpectedTokenException;
import io.github.therealmone.tdf4j.model.ast.AST;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
    static ParserGenerator generator = ParserGenerator.newInstance();
    static Lexer lexer;

    @BeforeClass
    public static void globalSetup() {
        lexer = LexerGenerator.newInstance().generate(new AbstractLexerModule() {
            @Override
            public void configure() {
                for (final TestTerminal testLexeme : TestTerminal.values()) {
                    tokenize(testLexeme.getTerminal().tag().value())
                            .pattern(testLexeme.getTerminal().pattern().pattern())
                            .priority(testLexeme.getTerminal().priority());
                }
                tokenize("ws").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
            }
        });
    }

    static Parser generate(final AbstractParserModule module) {
        final long current = System.currentTimeMillis();
        final Parser parser = generator.generate(module);
        System.out.println(parser.meta().sourceCode());
        System.out.println(module.getGrammar().toString());
        System.out.println(module.getGrammar().firstSet().toString());
        System.out.println(module.getGrammar().followSet().toString());
        System.out.println("Compilation time: " + (System.currentTimeMillis() - current));
        return parser;
    }

    static <T extends Parser> T generate(final AbstractParserModule module, final Class<T> interfaceToImplement) {
        final long current = System.currentTimeMillis();
        final T parser = generator.generate(module, interfaceToImplement);
        System.out.println(parser.meta().sourceCode());
        System.out.println(module.getGrammar().toString());
        System.out.println(module.getGrammar().firstSet().toString());
        System.out.println(module.getGrammar().followSet().toString());
        System.out.println("Compilation time: " + (System.currentTimeMillis() - current));
        return parser;
    }

    static AST parse(final Parser parser, final String input) {
        final long current = System.currentTimeMillis();
        final AST ast = parser.parse(lexer.stream(input));
        System.out.println("Parsing time: " + (System.currentTimeMillis() - current));
        System.out.println(ast);
        return ast;
    }

    static String unexpectedToken(final TestTerminal testTerminal) {
        return String.format("Unexpected token: Token{tag=%1$s, value=%1$s}", testTerminal.getTerminal().tag().value());
    }

    static String unexpectedEOF() {
        return "Unexpected token: null";
    }

    static void assertParserFails(final Parser parser, final String input, final String message) {
        assertThrows(() -> parse(parser, input), UnexpectedTokenException.class, message);
    }

    static void assertThrows(final Callback callback, final Class<? extends Throwable> ex, final String message) {
        try {
            callback.call();
            fail("Expected exception: " + ex.getName() + ": " + message);
        } catch (Exception e) {
            assertEquals(ex.getName(), e.getClass().getName());
            assertEquals(message, e.getMessage());
        }
    }

    @FunctionalInterface
    interface Callback {
        void call();
    }
}
