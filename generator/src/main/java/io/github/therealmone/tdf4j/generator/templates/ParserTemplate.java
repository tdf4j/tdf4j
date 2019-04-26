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
package io.github.therealmone.tdf4j.generator.templates;

import io.github.therealmone.tdf4j.commons.Environment;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

import java.util.List;

@Value.Immutable
public interface ParserTemplate extends Buildable {

    String pack();

    String imports();

    String className();

    Environment environment();

    String initProd();

    List<MethodTemplate> methods();

    @Override
    default String build() {
        final ST template = Template.PARSER.template()
                .add("package", pack())
                .add("imports", imports())
                .add("className", className())
                .add("environment", environment())
                .add("initProd", initProd());
        methods().forEach(method -> template.add("methods", method.build()));
        return template.render();
    }

    class Builder extends ImmutableParserTemplate.Builder {
    }
}
