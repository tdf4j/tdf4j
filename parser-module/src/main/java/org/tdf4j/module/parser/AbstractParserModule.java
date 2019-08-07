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
package org.tdf4j.module.parser;

import org.tdf4j.model.Environment;
import org.tdf4j.model.Module;
import org.tdf4j.model.First;
import org.tdf4j.model.Follow;
import org.tdf4j.model.Grammar;
import org.tdf4j.model.Production;
import org.tdf4j.utils.FirstSetCollector;
import org.tdf4j.utils.FollowSetCollector;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractParserModule extends BindingMapper implements Module {
    private final FirstSetCollector firstSetCollector = new FirstSetCollector();
    private final FollowSetCollector followSetCollector = new FollowSetCollector();
    private boolean built;
    private Grammar grammar;
    private Environment environment;

    public AbstractParserModule build() {
        if(!isBuilt()) {
            this.configure();
            final List<Production> productions = productionBindStrategy.build();
            this.environment = environmentBindStrategy.build();
            this.grammar = new Grammar.Builder()
                    .addAllProductions(productions)
                    .setAxiom(initProduction)
                    .setFirstSet(firstSetCollector.collect(productions))
                    .setFollowSet(followSetCollector.collect(productions))
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
