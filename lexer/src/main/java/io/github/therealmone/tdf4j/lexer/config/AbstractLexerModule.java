package io.github.therealmone.tdf4j.lexer.config;

import io.github.therealmone.tdf4j.commons.Module;
import io.github.therealmone.tdf4j.commons.bean.Terminal;
import java.util.*;

@SuppressWarnings("ALL")
public abstract class AbstractLexerModule extends BindingMapper implements Module {
    private final List<Terminal> terminals;
    private boolean built = false;

    public AbstractLexerModule() {
        terminals = new ArrayList<>();
    }

    public AbstractLexerModule build() {
        if(!built) {
            this.configure();
            this.terminals.addAll(terminalBindStrategy.build());
            built = true;
        }
        return this;
    }

    public List<Terminal> getTerminals() {
        return Collections.unmodifiableList(terminals);
    }
}
