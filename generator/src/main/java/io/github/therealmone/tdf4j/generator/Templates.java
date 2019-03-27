package io.github.therealmone.tdf4j.generator;

import org.stringtemplate.v4.ST;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

public enum Templates {
    TEST("templates/test.st");

    private final String resource;

    Templates(final String name) {
        this.resource = load(name);
    }

    @SuppressWarnings("ConstantConditions")
    private String load(final String path) {
        try(final InputStream inputStream = new BufferedInputStream(Templates.class.getClassLoader().getResourceAsStream(path));
            final Writer writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String resource() {
        return this.resource;
    }

    public ST template() {
        return new ST(this.resource);
    }
}
