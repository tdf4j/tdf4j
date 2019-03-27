package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class NonTerminal implements Element {
    @Override
    public Kind kind() {
        return Kind.NON_TERMINAL;
    }

    public abstract String identifier();

    public static class Builder extends ImmutableNonTerminal.Builder {

    }

    @Override
    public String toString() {
        return identifier();
    }
}
