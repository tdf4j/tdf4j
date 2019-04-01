package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Optional implements Element {
    @Override
    public Kind kind() {
        return Kind.OPTIONAL;
    }

    public abstract Element[] elements();

    public static class Builder extends ImmutableOptional.Builder {
    }

    @Override
    public String toString() {
        return "[" + toStringGroup(elements()) + "]";
    }
}
