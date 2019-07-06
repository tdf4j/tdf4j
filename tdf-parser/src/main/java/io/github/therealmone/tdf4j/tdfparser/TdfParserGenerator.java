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
package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.generator.Generator;
import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.generator.impl.ParserGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class TdfParserGenerator implements Generator<TdfParser> {
    private final String config;

    public TdfParserGenerator(final InputStream config) throws IOException {
        this.config = read(config);
    }

    public TdfParserGenerator(final String config) {
        this.config = config;
    }

    private String read(final InputStream input) throws IOException {
        try(final StringWriter writer = new StringWriter()) {
            int bt = 0;
            while((bt = input.read()) != -1) {
                writer.write(bt);
            }
            return writer.toString();
        }
    }

    @Override
    public TdfParser generate() {
        final Lexer lexer = new LexerGenerator(new TdfLexerModule()).generate();
        final TdfParser parser = new ParserGenerator(new TdfParserModule()).generate(TdfParser.class);
        parser.parse(lexer.stream(config));
        return parser;
    }

}
