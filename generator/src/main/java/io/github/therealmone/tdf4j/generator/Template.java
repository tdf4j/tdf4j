package io.github.therealmone.tdf4j.generator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public enum Template {
    PARSER("parser"),
    IMPORTS("imports"),
    METHOD("method"),
    LOGIC_TERMINAL("terminal_tag"),
    LOGIC_NON_TERMINAL("non_terminal"),
    LOGIC_OPTIONAL("optional"),
    LOGIC_OR("or"),
    LOGIC_REPEAT("repeat"),
    LOGIC_REPETITION("repetition"),
    LOGIC_GROUP("ele_group"),
    LOGIC_EXCEPT("except"),
    LOGIC_INLINE_ACTION("inline_action");

    private static final STGroup JAVA_TEMPLATE = new STGroupFile("templates/java.stg");

    static {
        JAVA_TEMPLATE.load();
    }

    private final String template;

    Template(final String template) {
        this.template = template;
    }

    public ST template() {
        return JAVA_TEMPLATE.getInstanceOf(template);
    }
}
