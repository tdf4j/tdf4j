package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.parser.model.Production;

public abstract class BindingMapper implements BindMethods {
    ProductionBindStrategy productionBindStrategy = new ProductionBindStrategy();

    @Override
    public Production.Builder prod(String identifier) {
        return productionBindStrategy.bind(identifier);
    }
}
