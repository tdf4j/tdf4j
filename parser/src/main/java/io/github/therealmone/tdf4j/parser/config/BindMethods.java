package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.Dependency;
import io.github.therealmone.tdf4j.commons.Environment;
import io.github.therealmone.tdf4j.commons.model.ebnf.*;

public interface BindMethods {
    void initProd(final String identifier);

    Production.Builder prod(final String identifier);

    Environment.Builder environment();

    <T> Dependency<T> dependency(final Class<T> clazz, final String name, final T instance);

    <T> Dependency<T> dependency(final Class<T> clazz, final String name);

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
