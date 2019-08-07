/*
 * Copyright (c) 2019 Roman Fatnev
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
package org.tdf4j.module.lexer;

import org.tdf4j.model.Module;
import org.tdf4j.model.ebnf.Terminal;
import java.util.*;

public abstract class AbstractLexerModule extends BindingMapper implements Module {
    private final List<Terminal> terminals;
    private boolean built = false;

    public AbstractLexerModule() {
        terminals = new ArrayList<>();
    }

    public AbstractLexerModule build() {
        if(!isBuilt()) {
            this.configure();
            this.terminals.addAll(terminalBindStrategy.build());
            built = true;
        }
        return this;
    }

    protected abstract void configure();

    public List<Terminal> getTerminals() {
        return Collections.unmodifiableList(terminals);
    }

    public boolean isBuilt() {
        return built;
    }
}
