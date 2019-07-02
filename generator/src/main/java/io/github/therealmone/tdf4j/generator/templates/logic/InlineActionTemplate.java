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

import io.github.therealmone.tdf4j.model.ebnf.InlineAction;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;

@SuppressWarnings("WeakerAccess")
@Value.Immutable
public abstract class InlineActionTemplate implements CodeBlock {

    public abstract InlineAction getInlineAction();

    @Override
    public String build() {
        return Template.LOGIC_INLINE_ACTION.template()
                .add("inlineAction", getInlineAction().getCode())
                .render();
    }

    public static class Builder extends ImmutableInlineActionTemplate.Builder {
    }
}
