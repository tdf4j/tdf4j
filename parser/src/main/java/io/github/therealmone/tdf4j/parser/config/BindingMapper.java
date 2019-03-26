package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.parser.model.*;

public abstract class BindingMapper implements BindMethods {
    ProductionBindStrategy productionBindStrategy = new ProductionBindStrategy();

    @Override
    public Production.Builder prod(final String identifier) {
        return productionBindStrategy.bind(identifier);
    }

    @Override
    public Optional optional(final Element ... elements) {
        return new Optional.Builder().elements(elements).build();
    }

    @Override
    public Group group(final Element ... elements) {
        return new Group.Builder().elements(elements).build();
    }

    @Override
    public Repeat repeat(final Element ... elements) {
        return new Repeat.Builder().elements(elements).build();
    }

    @Override
    public Or or(final Element first, final Element second) {
        return new Or.Builder().first(first).second(second).build();
    }

    @Override
    public Name name(final String value) {
        return new Name.Builder().value(value).build();
    }
}
