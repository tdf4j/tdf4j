package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;

public enum TestTerminal {
    A("A", 0),
    B("B", 0),
    C("C", 0);

    private final Terminal terminal;

    TestTerminal(final String tag, final long priority) {
        this.terminal = new Terminal.Builder().tag(tag).pattern(tag).priority(priority).build();
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
