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
package io.github.therealmone.tdf4j.generator.templates;

import io.github.therealmone.tdf4j.model.Environment;
import io.github.therealmone.tdf4j.generator.Template;
import io.github.therealmone.tdf4j.model.Grammar;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

import javax.annotation.Nullable;

@Value.Immutable
public abstract class ParserTemplate implements Buildable {

    public abstract String getPackage();

    public abstract String[] getImports();

    public abstract String getClassName();

    public abstract Environment getEnvironment();

    public abstract Grammar getGrammar();

    public abstract String getInterface();

    @Nullable
    public abstract String getExtension();

    @Override
    public String build() {
        final ST template = Template.JAVA.template().getInstanceOf("parser")
                .add("parserTemplate", this);
        return template.render();
    }

    public static class Builder extends ImmutableParserTemplate.Builder {
    }
}
