package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

@Value.Immutable
public interface Environment {

    String[] packages();

    Dependency[] dependencies();

    class Builder extends ImmutableEnvironment.Builder {
    }
}
