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
package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.templates.ElementRenderer;
import io.github.therealmone.tdf4j.generator.templates.adaptor.*;
import io.github.therealmone.tdf4j.model.ebnf.*;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public enum Template {
    JAVA(new STGroupFile("templates/java_1.1.stg"));

    private final STGroup stGroup;

    Template(final STGroup stGroup) {
        this.stGroup = stGroup;
        stGroup.load();
        stGroup.registerModelAdaptor(Optional.class, new OptionalAdaptor());
        stGroup.registerModelAdaptor(Or.class, new OrAdaptor());
        stGroup.registerModelAdaptor(Repeat.class, new RepeatAdaptor());
        stGroup.registerModelAdaptor(Repetition.class, new RepetitionAdaptor());
        stGroup.registerModelAdaptor(NonTerminal.class, new NonTerminalAdaptor());
        stGroup.registerModelAdaptor(Terminal.Tag.class, new TerminalAdaptor());
        stGroup.registerModelAdaptor(Group.class, new GroupAdaptor());
        stGroup.registerModelAdaptor(InlineAction.class, new InlineActionAdaptor());
        stGroup.registerRenderer(Element.class, new ElementRenderer());
    }

    public STGroup template() {
        return stGroup;
    }
}
