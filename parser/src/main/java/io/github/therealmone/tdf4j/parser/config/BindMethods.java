package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.parser.model.*;

public interface BindMethods {
    Production.Builder prod(final String identifier);

    Optional optional(final Element ... elements);

    Group group(final Element ... elements);

    Repeat repeat(final Element ... elements);

    Or or(final Element first, final Element second);

    Name name(final String value);
}
