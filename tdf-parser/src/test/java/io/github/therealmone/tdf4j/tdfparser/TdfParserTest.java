package io.github.therealmone.tdf4j.tdfparser;


import io.github.therealmone.tdf4j.tdfparser.impl.TdfInterpreter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class TdfParserTest {

    Interpreter generate(final String fileName) {
        final Interpreter interpreter = new TdfInterpreter();
        interpreter.parse(load(fileName));
        return interpreter;
    }

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
