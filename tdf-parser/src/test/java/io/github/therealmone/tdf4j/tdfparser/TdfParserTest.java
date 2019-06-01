package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class TdfParserTest {
    final TdfParser tdfParser = new ParserGenerator(new ParserModule()).generate(TdfParser.class);
    final Lexer tdfLexer = new LexerGenerator(new LexerModule()).generate();

    @SuppressWarnings("ConstantConditions")
    String load(final String fileName) {
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
