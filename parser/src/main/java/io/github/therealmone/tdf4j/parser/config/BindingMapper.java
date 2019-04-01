package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.model.ebnf.*;

public abstract class BindingMapper implements BindMethods {
    ProductionBindStrategy productionBindStrategy = new ProductionBindStrategy();
    String initProduction;

    @Override
    public Production.Builder prod(final String identifier) {
        if(initProduction == null) {
            initProduction = identifier;
        }
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
    public Repetition repetition(final Element element, final int times) {
        return new Repetition.Builder().element(element).times(times).build();
    }

    @Override
    public Or or(final Element first, final Element second) {
        return new Or.Builder().first(first).second(second).build();
    }

    @Override
    public Or oneOf(Element... elements) {
        if(elements.length < 2) {
            throw new RuntimeException("oneOf() accepts 2 ore more elements");
        }

        if(elements.length == 2) {
            return new Or.Builder().first(elements[0]).second(elements[1]).build();
        } else {
            final Element[] toRecursion = new Element[elements.length - 1];
            System.arraycopy(elements, 1, toRecursion, 0, elements.length - 1);
            return new Or.Builder().first(elements[0]).second(oneOf(toRecursion)).build();
        }
    }

    @Override
    public Terminal.Tag t(final String tag) {
        return new Terminal.Tag.Builder().value(tag).build();
    }

    @Override
    public NonTerminal nt(final String identifier) {
        return new NonTerminal.Builder().identifier(identifier).build();
    }

    @Override
    public void initProd(final String identifier) {
        this.initProduction = identifier;
    }

    @Override
    public Except except(final Terminal.Tag ... tags) {
        return new Except.Builder().tags(tags).build();
    }
}
