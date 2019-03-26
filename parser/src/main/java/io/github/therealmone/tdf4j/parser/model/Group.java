package io.github.therealmone.tdf4j.parser.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Group implements Element {
    @Override
    public Kind kind() {
        return Kind.GROUP;
    }

    public abstract Element[] elements();

    public static class Builder extends ImmutableGroup.Builder {

    }

    @Override
    public String toString() {
        return "(" + toStringGroup(elements()) + ")";
    }
}
