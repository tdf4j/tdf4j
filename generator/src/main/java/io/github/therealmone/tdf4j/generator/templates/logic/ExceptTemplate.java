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
package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Except;
import io.github.therealmone.tdf4j.commons.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface ExceptTemplate extends CodeBlock {

    Except except();

    @Override
    default String build() {
        final ST template = Template.LOGIC_EXCEPT.template();
        for(final Terminal.Tag tag : except().tags()) {
            template.add("exceptions", tag.value());
        }
        return template.render();
    }

    class Builder extends ImmutableExceptTemplate.Builder {
    }
}
