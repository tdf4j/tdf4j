package io.github.therealmone.tdf4j.parser.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Production {

    String identifier();

    class Builder extends ImmutableProduction.Builder {

    }
}
