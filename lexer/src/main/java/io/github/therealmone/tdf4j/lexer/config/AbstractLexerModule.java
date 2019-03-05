package io.github.therealmone.tdf4j.lexer.config;

import io.github.therealmone.tdf4j.commons.Module;
import io.github.therealmone.tdf4j.commons.bean.Terminal;
import java.util.*;

@SuppressWarnings("ALL")
public abstract class AbstractLexerModule extends BindingMapper implements Module {
    private final List<Terminal> terminals;

    public AbstractLexerModule() {
        terminals = new ArrayList<>();
        configure();
        build();
    }

    private final void build() {
        this.terminals.addAll(terminalBindStrategy.build());
    }

    public List<Terminal> getTerminals() {
        return Collections.unmodifiableList(terminals);
    }
}
