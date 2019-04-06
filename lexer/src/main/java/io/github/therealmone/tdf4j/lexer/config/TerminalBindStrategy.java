package io.github.therealmone.tdf4j.lexer.config;

import io.github.therealmone.tdf4j.commons.BindStrategy;
import io.github.therealmone.tdf4j.commons.model.ebnf.ImmutableTerminal;
import io.github.therealmone.tdf4j.commons.model.ebnf.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class TerminalBindStrategy implements BindStrategy<String, Terminal.Builder, List<Terminal>> {
    private final Map<String, Terminal.Builder> builders = new HashMap<>();

    @Override
    public Terminal.Builder bind(final String key) {
        //noinspection ConstantConditions
        if(key == null || key.trim().equals("")) {
            throw new RuntimeException("Tag can't be null or blank");
        }
        if(builders.containsKey(key)) {
            throw new RuntimeException("Key " + key + " already bind");
        }
        builders.put(key, new Terminal.Builder().tag(key));
        return builders.get(key);
    }

    @Override
    public List<Terminal> build() {
        return builders.values().stream().map(ImmutableTerminal.Builder::build).collect(Collectors.toList());
    }
}
