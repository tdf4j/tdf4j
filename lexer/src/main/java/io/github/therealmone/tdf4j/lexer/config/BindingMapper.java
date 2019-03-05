package io.github.therealmone.tdf4j.lexer.config;

import io.github.therealmone.tdf4j.commons.bean.Terminal;
import io.github.therealmone.tdf4j.lexer.config.strategies.BindStrategy;
import io.github.therealmone.tdf4j.lexer.config.strategies.TerminalBindStrategy;

import java.util.List;

public abstract class BindingMapper implements BindMethods {
    final BindStrategy<String, Terminal.Builder, List<Terminal>> terminalBindStrategy = new TerminalBindStrategy();

    @Override
    public Terminal.Builder tokenize(String tag) {
        return terminalBindStrategy.bind(tag);
    }
}
