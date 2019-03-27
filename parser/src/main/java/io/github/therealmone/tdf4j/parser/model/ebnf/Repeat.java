package io.github.therealmone.tdf4j.parser.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Repeat implements Element {
    @Override
    public Kind kind() {
        return Kind.REPEAT;
    }

    public abstract Element[] elements();

    public static class Builder extends ImmutableRepeat.Builder {

    }

    @Override
    public String toString() {
        return "{" + toStringGroup(elements()) + "}";
    }
}
