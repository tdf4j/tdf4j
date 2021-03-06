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
package io.github.tdf4j.core.module;

import io.github.tdf4j.core.model.Environment;
import io.github.tdf4j.core.model.First;
import io.github.tdf4j.core.model.Follow;
import io.github.tdf4j.core.model.Grammar;
import io.github.tdf4j.core.model.Production;
import io.github.tdf4j.core.utils.FirstSetCollector;
import io.github.tdf4j.core.utils.FollowSetCollector;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ParserAbstractModule extends ParserBindingMapper implements Module {
    private boolean built;
    private Grammar grammar;
    private Environment environment;

    public ParserAbstractModule build() {
        if(!isBuilt()) {
            this.configure();
            final List<Production> productions = productionBindStrategy.build();
            this.environment = environmentBindStrategy.build();
            this.grammar = new Grammar.Builder()
                    .addAllProductions(productions)
                    .setAxiom(initProduction)
                    .setFirstSet(FirstSetCollector.collect(productions))
                    .setFollowSet(FollowSetCollector.collect(productions))
                    .build();
            built = true;
        }
        return this;
    }

    @Nonnull
    public Grammar getGrammar() {
        return grammar != null
                ? grammar
                : new Grammar.Builder()
                    .setFirstSet(new First.Builder().build())
                    .setFollowSet(new Follow.Builder().build())
                    .build();
    }

    @Nonnull
    public Environment getEnvironment() {
        return environment != null
                ? environment
                : new Environment.Builder().build();
    }

    protected abstract void configure();

    public boolean isBuilt() {
        return built;
    }
}
