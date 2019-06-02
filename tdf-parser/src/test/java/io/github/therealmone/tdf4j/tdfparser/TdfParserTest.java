package io.github.therealmone.tdf4j.tdfparser;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class TdfParserTest {

    TdfParser generate(final String fileName) {
        try {
            //noinspection ConstantConditions
            return new TdfParserGenerator(
                    new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName))
            ).generate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
