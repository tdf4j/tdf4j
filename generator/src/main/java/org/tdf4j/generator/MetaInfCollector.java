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

import org.tdf4j.core.model.Dependency;
import org.tdf4j.generator.templates.ParserTemplate;
import org.tdf4j.parser.MetaInf;

import java.util.ArrayList;
import java.util.List;

public class MetaInfCollector {

    public MetaInf collect(ParserTemplate parserTemplate) {
        final MetaInf.Builder builder = new MetaInf.Builder()
                .setPackage(parserTemplate.getPackage())
                .setClassName(parserTemplate.getClassName())
                .setSourceCode(parserTemplate.build())
                .setEnvironmentImports(parserTemplate.getEnvironment().getPackages())
                .setDependencies(collectDependencies(parserTemplate))
                .setImports(collectImports(parserTemplate));

        return builder.build();
    }

    private String[] collectImports(final ParserTemplate parserTemplate) {
        final List<String> imports = new ArrayList<>();
        for (String imp :parserTemplate.getImports()){
            imports.add(imp.replaceAll(";|\n|;\n|\r|;\r", ""));
        }
        return imports.toArray(new String[]{});
    }

    private String[] collectDependencies(final ParserTemplate parserTemplate) {
        final String[] dependencies = new String[parserTemplate.getEnvironment().getDependencies().length];
        for (int i = 0; i < parserTemplate.getEnvironment().getDependencies().length; i++) {
            final Dependency dependency = parserTemplate.getEnvironment().getDependencies()[i];
            dependencies[i] = dependency.getClazz().getCanonicalName();
        }
        return dependencies;
    }
}
