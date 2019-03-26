package io.github.therealmone.tdf4j.parser.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Name implements Element {
    @Override
    public Kind kind() {
        return Kind.NAME;
    }

    public abstract String value();

    public static class Builder extends ImmutableName.Builder {

    }

    @Override
    public String toString() {
        return value();
    }
}
