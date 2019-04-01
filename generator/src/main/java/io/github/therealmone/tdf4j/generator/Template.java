package io.github.therealmone.tdf4j.generator;

import org.stringtemplate.v4.ST;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

public enum Template {
    PARSER("templates/parser.st"),
    IMPORTS("templates/imports.st"),
    METHOD("templates/method.st"),
    LOGIC_TERMINAL("templates/logic/terminal_tag.st"),
    LOGIC_NON_TERMINAL("templates/logic/non_terminal.st"),
    LOGIC_OPTIONAL("templates/logic/optional.st"),
    LOGIC_OR("templates/logic/or.st"),
    LOGIC_REPEAT("templates/logic/repeat.st"),
    LOGIC_REPETITION("templates/logic/repetition.st"),
    LOGIC_GROUP("templates/logic/group.st"),
    LOGIC_EXCEPT("templates/logic/except.st");

    private final String resource;

    Template(final String name) {
        this.resource = load(name);
    }

    @SuppressWarnings("ConstantConditions")
    private String load(final String path) {
        try(final InputStream inputStream = new BufferedInputStream(Template.class.getClassLoader().getResourceAsStream(path));
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
