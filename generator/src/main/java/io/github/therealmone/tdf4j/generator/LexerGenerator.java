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
package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.LexerGeneratorImpl;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.SymbolListener;
import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import org.json.simple.parser.ParseException;

import java.io.*;

public interface LexerGenerator extends Generator<Lexer, AbstractLexerModule> {

    Lexer fromXml(final String xml, final SymbolListener listener);

    Lexer fromXml(final String xml);

    Lexer fromXml(final InputStream inputStream, final SymbolListener listener) throws IOException;

    Lexer fromXml(final InputStream inputStream) throws IOException;

    Lexer fromXml(final File file, final SymbolListener listener) throws IOException;

    Lexer fromXml(final File file) throws IOException;

    Lexer fromJson(final String json, final SymbolListener listener) throws ParseException;

    Lexer fromJson(final String json) throws ParseException;

    Lexer fromJson(final InputStream inputStream, final SymbolListener listener) throws IOException, ParseException;

    Lexer fromJson(final InputStream inputStream) throws IOException, ParseException;

    Lexer fromJson(final File file, final SymbolListener listener) throws IOException, ParseException;

    Lexer fromJson(final File file) throws IOException, ParseException;

    Lexer generate(final AbstractLexerModule module, final SymbolListener listener);

    static LexerGenerator newInstance() {
        return new LexerGeneratorImpl();
    }
}
