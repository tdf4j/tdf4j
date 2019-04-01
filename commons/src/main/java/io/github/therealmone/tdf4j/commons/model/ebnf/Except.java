package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Except implements Element {
    @Override
    public Kind kind() {
        return Kind.EXCEPT;
    }

    public abstract Terminal.Tag[] tags();

    public static class Builder extends ImmutableExcept.Builder {
    }

    @Override
    public String toString() {
        return "-(" + (toStringGroup(tags())) + ")";
    }
}
