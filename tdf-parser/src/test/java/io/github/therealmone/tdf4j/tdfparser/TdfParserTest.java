package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.LexerGenerator;
import io.github.therealmone.tdf4j.generator.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class TdfParserTest {
    final TdfParser tdfParser = ParserGenerator.newInstance().generate(new ParserModule(), TdfParser.class);
    final Lexer tdfLexer = LexerGenerator.newInstance().generate(new LexerModule());

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
