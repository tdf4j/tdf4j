package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Repetition implements Element {
    @Override
    public Kind kind() {
        return Kind.REPETITION;
    }

    public abstract Element element();

    public abstract int times();

    public static class Builder extends ImmutableRepetition.Builder {
    }

    @Override
    public String toString() {
        return times() + "*" + element();
    }
}
