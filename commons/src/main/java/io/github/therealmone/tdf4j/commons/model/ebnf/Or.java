package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Or implements Element {
    @Override
    public Kind kind() {
        return Kind.OR;
    }

    public abstract Element first();

    public abstract Element second();

    public static class Builder extends ImmutableOr.Builder {

    }

    @Override
    public String toString() {
        return first().toString() + "|" + second().toString();
    }
}
