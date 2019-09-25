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
package org.tdf4j.generator.impl;

import org.tdf4j.generator.Options;
import org.tdf4j.core.model.Dependency;
import org.tdf4j.generator.Generator;
import org.tdf4j.core.utils.Predictor;
import org.tdf4j.generator.Imports;
import org.tdf4j.generator.templates.ParserTemplate;
import org.tdf4j.generator.MetaInfCollector;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.parser.MetaInf;
import org.tdf4j.parser.Parser;
import org.joor.Reflect;


public class ParserGenerator implements Generator<Parser> {
    private final MetaInfCollector metaInfCollector = new MetaInfCollector();
    private final Options options;

    public ParserGenerator(final Options options) {
        this.options = options;
        options.getParserModule().build();
    }

    @Override
    public Parser generate() {
        final ParserTemplate parser = build(options);
        return Reflect.compile(options.getPackage() + "." + options.getClassName(),
                parser.build()
        ).create(args(
                metaInfCollector.collect(parser),
                Lexer.get(options.getLexerModule()),
                new Predictor(options.getParserModule().getGrammar().getFirstSet(), options.getParserModule().getGrammar().getFollowSet()),
                options.getParserModule().getEnvironment().getDependencies()
        )).get();
    }

    private ParserTemplate build(final Options options) {
        final ParserTemplate.Builder parserBuilder = new ParserTemplate.Builder()
                .setClassName(options.getClassName())
                .setPackage(options.getPackage())
                .setEnvironment(options.getParserModule().getEnvironment())
                .setImports(imports(options.getInterface().getCanonicalName()))
                .setInterface(options.getInterface().getSimpleName())
                .setExtension(options.getExtension() != null ? options.getExtension().getCanonicalName() : null);
        if(options.getParserModule().getGrammar().getAxiom() == null) {
            throw new RuntimeException("Initial production is null");
        }
        parserBuilder.setGrammar(options.getParserModule().getGrammar());
        return parserBuilder.build();
    }

    private Object[] args(final MetaInf meta, final Lexer lexer, final Predictor predictor, final Dependency[] dependencies) {
        final Object[] args = new Object[dependencies.length + 3];
        args[0] = meta;
        args[1] = lexer;
        args[2] = predictor;
        for (int i = 0; i < dependencies.length; i++) {
            args[i + 3] = dependencies[i].instance();
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
