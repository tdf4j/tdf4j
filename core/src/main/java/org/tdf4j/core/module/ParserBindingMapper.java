/*
 * Copyright (c) 2019 tdf4j
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
package org.tdf4j.core.module;

import org.tdf4j.core.model.Dependency;
import org.tdf4j.core.model.Environment;
import org.tdf4j.core.model.Production;
import org.tdf4j.core.SyntaxHighlight;
import org.tdf4j.core.model.ebnf.*;

public abstract class ParserBindingMapper implements ParserBindMethods {
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
        return new Dependency.Builder<T>().setClazz(clazz).setName(name).setInstance(instance).build();
    }

    @Override
    public <T> Dependency<T> dependency(final Class<T> clazz, final String name) {
        return new Dependency.Builder<T>().setClazz(clazz).setName(name).build();
    }

    @Override
    public Optional optional(final Element... elements) {
        return EBNFBuilder.optional(elements);
    }

    @Override
    public Group group(final Element ... elements) {
        return EBNFBuilder.group(elements);
    }

    @Override
    public Repeat repeat(final Element ... elements) {
        return EBNFBuilder.repeat(elements);
    }

    @Override
    public Repetition repetition(final Element element, final int times) {
        return EBNFBuilder.repetition(element, times);
    }

    @Override
    public Or or(final Element ... alternatives) {
        return EBNFBuilder.or(alternatives);
    }

    @Override
    public Terminal t(final String tag) {
        return EBNFBuilder.terminal(tag);
    }

    @Override
    public Terminal t(final String tag, @SyntaxHighlight.TokenAction final String tokenAction) {
        return EBNFBuilder.terminal(tag, tokenAction);
    }

    @Override
    public NonTerminal nt(final String identifier) {
        return EBNFBuilder.nonTerminal(identifier);
    }

    @Override
    public NonTerminal nt(final String identifier, @SyntaxHighlight.NodeAction final String nodeAction) {
        return EBNFBuilder.nonTerminal(identifier, nodeAction);
    }

    @Override
    public void axiom(final String identifier) {
        this.initProduction = identifier;
    }

    @Override
    public InlineAction inline(@SyntaxHighlight.InlineAction final String code) {
        //noinspection ConstantConditions
        if(code == null || code.trim().equalsIgnoreCase("")) {
            throw new IllegalStateException("Code can't be blank or null");
        }
        return EBNFBuilder.inlineAction(code);
    }
}
