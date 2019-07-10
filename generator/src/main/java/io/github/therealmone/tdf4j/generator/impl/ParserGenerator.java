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
package io.github.therealmone.tdf4j.generator.impl;

import io.github.therealmone.tdf4j.generator.ParserOptions;
import io.github.therealmone.tdf4j.model.Dependency;
import io.github.therealmone.tdf4j.generator.Generator;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.utils.Predictor;
import io.github.therealmone.tdf4j.generator.Imports;
import io.github.therealmone.tdf4j.generator.templates.ParserTemplate;
import io.github.therealmone.tdf4j.generator.MetaInfCollector;
import io.github.therealmone.tdf4j.parser.MetaInf;
import io.github.therealmone.tdf4j.parser.Parser;
import org.joor.Reflect;


public class ParserGenerator implements Generator<Parser> {
    private final MetaInfCollector metaInfCollector = new MetaInfCollector();
    private final ParserOptions options;

    public ParserGenerator(final ParserOptions options) {
        this.options = options;
        options.getModule().build();
    }

    @Override
    public Parser generate() {
        final ParserTemplate parser = build(
                options.getModule(),
                options.getClassName(),
                options.getPackage(),
                options.getInterface()
        );
        return Reflect.compile(options.getPackage() + "." + options.getClassName(),
                parser.build()
        ).create(args(
                metaInfCollector.collect(parser),
                new Predictor(options.getModule().getGrammar().getFirstSet(), options.getModule().getGrammar().getFollowSet()),
                options.getModule().getEnvironment().getDependencies()
        )).get();
    }

    private ParserTemplate build(final AbstractParserModule module, final String className, final String pack, final Class<? extends Parser> interfaceToImplement) {
        final ParserTemplate.Builder parserBuilder = new ParserTemplate.Builder()
                .setClassName(className)
                .setPackage(pack)
                .setEnvironment(module.getEnvironment())
                .setImports(imports(interfaceToImplement.getCanonicalName()))
                .setInterface(interfaceToImplement.getSimpleName());
        if(module.getGrammar().getAxiom() == null) {
            throw new RuntimeException("Initial production is null");
        }
        parserBuilder.setGrammar(module.getGrammar());
        return parserBuilder.build();
    }

    private Object[] args(final MetaInf meta, final Predictor predictor, final Dependency[] dependencies) {
        final Object[] args = new Object[dependencies.length + 2];
        args[0] = meta;
        args[1] = predictor;
        for (int i = 0; i < dependencies.length; i++) {
            args[i + 2] = dependencies[i].instance();
        }
        return args;
    }

    private String[] imports(final String... additional) {
        final String[] imports = new String[Imports.values().length + additional.length];
        for (int i = 0; i < Imports.values().length; i++) {
            imports[i] = Imports.values()[i].getValue();
        }
        System.arraycopy(additional, 0, imports, Imports.values().length, additional.length);
        return imports;
    }
}
