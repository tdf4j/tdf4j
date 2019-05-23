/*
 * Copyright 2019 Roman Fatnev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.therealmone.tdf4j.module.parser;

import io.github.therealmone.tdf4j.commons.Dependency;
import io.github.therealmone.tdf4j.commons.Environment;
import io.github.therealmone.tdf4j.model.ebnf.*;

public abstract class BindingMapper implements BindMethods {
    ProductionBindStrategy productionBindStrategy = new ProductionBindStrategy();
    EnvironmentBindStrategy environmentBindStrategy = new EnvironmentBindStrategy();
    String initProduction;

    @Override
    public Production.Builder prod(final String identifier) {
        if(initProduction == null) {
            initProduction = identifier;
        }
        return productionBindStrategy.bind(identifier);
    }

    @Override
    public Environment.Builder environment() {
        return environmentBindStrategy.bind();
    }

    @Override
    public <T> Dependency<T> dependency(final Class<T> clazz, final String name, final T instance) {
        return new Dependency.Builder<T>().clazz(clazz).name(name).instance(instance).build();
    }

    @Override
    public <T> Dependency<T> dependency(final Class<T> clazz, final String name) {
        return new Dependency.Builder<T>().clazz(clazz).name(name).build();
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
    public Or oneOf(final Element... elements) {
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

    @Override
    public InlineAction inline(final String code) {
        //noinspection ConstantConditions
        if(code == null || code.trim().equalsIgnoreCase("")) {
            throw new IllegalStateException("Code can't be blank or null");
        }
        return new InlineAction.Builder().code(code).build();
    }
}
