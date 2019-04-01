package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.model.ebnf.*;

public interface BindMethods {
    void initProd(final String identifier);

    Production.Builder prod(final String identifier);

    Optional optional(final Element ... elements);

    Group group(final Element ... elements);

    Repeat repeat(final Element ... elements);

    Repetition repetition(final Element element, final int times);

    Or or(final Element first, final Element second);

    Or oneOf(final Element ... elements);

    Terminal.Tag t(final String tag);

    NonTerminal nt(final String identifier);

    Except except(final Terminal.Tag ... tags);
}
