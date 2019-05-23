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
package io.github.therealmone.tdf4j.generator.impl;

import io.github.therealmone.tdf4j.generator.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.SymbolListener;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.impl.SymbolListenerImpl;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.lexer.JsonLexerModule;
import io.github.therealmone.tdf4j.module.lexer.XmlLexerModule;
import org.json.simple.parser.ParseException;

import java.io.*;

public class LexerGeneratorImpl implements LexerGenerator {

    @Override
    public Lexer fromXml(final String xml, final SymbolListener listener) {
        return new LexerImpl(new XmlLexerModule(xml).build(), listener);
    }

    @Override
    public Lexer fromXml(final String xml) {
        return fromXml(xml, new SymbolListenerImpl());
    }

    @Override
    public Lexer fromXml(final InputStream inputStream, final SymbolListener listener) throws IOException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromXml(writer.toString(), listener);
        }
    }

    @Override
    public Lexer fromXml(final InputStream inputStream) throws IOException {
        return fromXml(inputStream, new SymbolListenerImpl());
    }

    @Override
    public Lexer fromXml(final File file, final SymbolListener listener) throws IOException{
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromXml(inputStream, listener);
        }
    }

    @Override
    public Lexer fromXml(final File file) throws IOException{
        return fromXml(file, new SymbolListenerImpl());
    }

    @Override
    public Lexer fromJson(final String json, final SymbolListener listener) throws ParseException {
        return new LexerImpl(new JsonLexerModule(json).build(), listener);
    }

    @Override
    public Lexer fromJson(final String json) throws ParseException {
        return fromJson(json, new SymbolListenerImpl());
    }

    @Override
    public Lexer fromJson(final InputStream inputStream, final SymbolListener listener) throws IOException, ParseException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromJson(writer.toString(), listener);
        }
    }

    @Override
    public Lexer fromJson(final InputStream inputStream) throws IOException, ParseException {
        return fromJson(inputStream, new SymbolListenerImpl());
    }

    @Override
    public Lexer fromJson(final File file, final SymbolListener listener) throws IOException, ParseException {
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromJson(inputStream, listener);
        }
    }

    @Override
    public Lexer fromJson(final File file) throws IOException, ParseException {
        return fromJson(file, new SymbolListenerImpl());
    }

    @Override
    public Lexer generate(final AbstractLexerModule module, final SymbolListener listener) {
        return new LexerImpl(module.build(), listener);
    }

    @Override
    public Lexer generate(final AbstractLexerModule module) {
        return generate(module, new SymbolListenerImpl());
    }
}
