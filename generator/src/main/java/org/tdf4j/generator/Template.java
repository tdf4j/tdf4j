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
package org.tdf4j.generator;

import org.tdf4j.core.model.ebnf.*;
import org.tdf4j.generator.templates.ElementRenderer;
import org.tdf4j.generator.templates.adaptor.*;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public enum Template {
    JAVA(new STGroupFile("templates/java_1.2.stg"));

    private final STGroup stGroup;

    Template(final STGroup stGroup) {
        this.stGroup = stGroup;
        stGroup.load();
        stGroup.registerModelAdaptor(Optional.class, new OptionalAdaptor());
        stGroup.registerModelAdaptor(Repeat.class, new RepeatAdaptor());
        stGroup.registerModelAdaptor(Repetition.class, new RepetitionAdaptor());
        stGroup.registerModelAdaptor(NonTerminal.class, new NonTerminalAdaptor());
        stGroup.registerModelAdaptor(Terminal.class, new TerminalAdaptor());
        stGroup.registerModelAdaptor(Group.class, new GroupAdaptor());
        stGroup.registerModelAdaptor(InlineAction.class, new InlineActionAdaptor());
        stGroup.registerModelAdaptor(Or.class, new OrAdaptor());
        stGroup.registerModelAdaptor(Alternative.class, new AlternativeAdaptor());
        stGroup.registerRenderer(Element.class, new ElementRenderer());
    }

    public STGroup template() {
        return stGroup;
    }
}
