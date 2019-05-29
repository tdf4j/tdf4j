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

import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Group;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public abstract class GroupTemplate implements CodeBlock {

    public abstract Group group();

    @Override
    public String build() {
        final ST template = Template.LOGIC_GROUP.template()
                .add("hash", Math.abs(this.hashCode()));
        for(final Element element : group().elements()) {
            final CodeBlock codeBlock = CodeBlock.fromElement(element);
            if(codeBlock != null) {
                template.add("codeBlocks", codeBlock.build());
            }
        }
        return template.render();
    }

    public static class Builder extends ImmutableGroupTemplate.Builder {
    }
}
