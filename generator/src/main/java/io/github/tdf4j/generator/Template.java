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
package io.github.tdf4j.generator;

import io.github.tdf4j.core.model.Letter;
import io.github.tdf4j.core.model.ebnf.*;
import io.github.tdf4j.generator.templates.renderer.ElementRenderer;
import io.github.tdf4j.generator.templates.adaptor.*;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import io.github.tdf4j.generator.templates.renderer.PatternRenderer;

import java.util.regex.Pattern;

public enum Template {
    JAVA(new STGroupFile("templates/java_1.3.stg"));

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
        stGroup.registerModelAdaptor(Letter.class, new LetterAdaptor());
        stGroup.registerRenderer(Element.class, new ElementRenderer());
        stGroup.registerRenderer(Pattern.class, new PatternRenderer());
    }

    public STGroup template() {
        return stGroup;
    }
}
