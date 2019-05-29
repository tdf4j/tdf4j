package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.LexerGenerator;
import io.github.therealmone.tdf4j.generator.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.model.ast.AST;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class GrammarTest {
    private final Parser parser = ParserGenerator.newInstance().generate(new ParserModule());
    private final Lexer lexer = LexerGenerator.newInstance().generate(new LexerModule());

    @Test
    public void test() {
        System.out.println(parser.meta().sourceCode());
        System.out.println(new ParserModule().build().getGrammar());
        System.out.println(new ParserModule().build().getGrammar().firstSet());
        System.out.println(new ParserModule().build().getGrammar().followSet());
        final AST ast = parser.parse(lexer.stream(load("test.tdf")));
        assertNotNull(ast);
        System.out.println(ast);
    }

    @SuppressWarnings("ConstantConditions")
    private String load(final String fileName) {
        try(final InputStream inputStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
            final StringWriter writer = new StringWriter())
        {
            int bt = 0;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
