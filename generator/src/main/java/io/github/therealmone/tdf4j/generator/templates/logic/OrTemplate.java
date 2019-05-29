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

import io.github.therealmone.tdf4j.model.ebnf.Or;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public abstract class OrTemplate extends Prediction implements CodeBlock {

    public abstract Or or();

    @Override
    public String build() {
        final ST template = Template.LOGIC_OR.template();
        final CodeBlock first = CodeBlock.fromElement(or().first());
        if(first != null) {
            template.add("firstStartElements", getStartElements(or().first()));
            template.add("firstCodeBlocks", first.build());
        }
        final CodeBlock second = CodeBlock.fromElement(or().second());
        if(second != null) {
            template.add("secondStartElements", getStartElements(or().second()));
            template.add("secondCodeBlocks", second.build());
        }
        return template.render();
    }

    public static class Builder extends ImmutableOrTemplate.Builder {
    }
}
