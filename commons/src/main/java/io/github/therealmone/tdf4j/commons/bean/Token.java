package io.github.therealmone.tdf4j.commons.bean;

import org.immutables.value.Value;

@Value.Immutable
public interface Token {
    String tag();

    String value();

    class Builder extends ImmutableToken.Builder {

    }
}
