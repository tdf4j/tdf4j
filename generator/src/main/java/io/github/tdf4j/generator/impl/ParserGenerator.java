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
package io.github.tdf4j.generator.impl;

import io.github.tdf4j.generator.Options;
import io.github.tdf4j.generator.Generator;
import io.github.tdf4j.generator.Imports;
import io.github.tdf4j.generator.templates.ParserTemplate;
import io.github.tdf4j.parser.ParserMetaInformation;


public class ParserGenerator implements Generator<ParserMetaInformation> {
    private final Options options;

    public ParserGenerator(final Options options) {
        this.options = options;
        options.getParserModule().build();
        options.getLexerModule().build();
    }

    @Override
    public ParserMetaInformation generate() {
        final ParserTemplate parser = buildParserTemplate(options);
        return collectMetaInformation(parser);
    }

    private ParserMetaInformation collectMetaInformation(final ParserTemplate parserTemplate) {
        return new ParserMetaInformation.Builder()
                .setPackage(parserTemplate.getPackage())
                .setClassName(parserTemplate.getClassName())
                .setSourceCode(parserTemplate.build())
                .setEnvironment(parserTemplate.getEnvironment())
                .setImports(parserTemplate.getImports())
                .setGrammar(parserTemplate.getGrammar())
                .build();
    }

    private ParserTemplate buildParserTemplate(final Options options) {
        final ParserTemplate.Builder parserBuilder = new ParserTemplate.Builder()
                .setClassName(options.getClassName())
                .setPackage(options.getPackage())
                .setEnvironment(options.getParserModule().getEnvironment())
                .setImports(imports(options.getInterface().getCanonicalName()))
                .setInterface(options.getInterface().getSimpleName())
                .setAlphabet(options.getLexerModule().getAlphabet());
        if(options.getParserModule().getGrammar().getAxiom() == null) {
            throw new RuntimeException("Initial production is null");
        }
        parserBuilder.setGrammar(options.getParserModule().getGrammar());
        return parserBuilder.build();
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
