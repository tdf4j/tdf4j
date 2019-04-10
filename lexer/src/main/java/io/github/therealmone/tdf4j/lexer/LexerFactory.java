package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import io.github.therealmone.tdf4j.lexer.config.JsonLexerModule;
import io.github.therealmone.tdf4j.lexer.config.XmlLexerModule;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.impl.SymbolListenerImpl;
import org.json.simple.parser.ParseException;

import java.io.*;

public class LexerFactory{
    public Lexer fromXml(final String xml, final SymbolListener listener) {
        return new LexerImpl(new XmlLexerModule(xml).build(), listener);
    }

    public Lexer fromXml(final String xml) {
        return fromXml(xml, new SymbolListenerImpl());
    }

    public Lexer fromXml(final InputStream inputStream, final SymbolListener listener) throws IOException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromXml(writer.toString(), listener);
        }
    }

    public Lexer fromXml(final InputStream inputStream) throws IOException {
        return fromXml(inputStream, new SymbolListenerImpl());
    }

    public Lexer fromXml(final File file, final SymbolListener listener) throws IOException{
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromXml(inputStream, listener);
        }
    }

    public Lexer fromXml(final File file) throws IOException{
        return fromXml(file, new SymbolListenerImpl());
    }

    public Lexer fromJson(final String json, final SymbolListener listener) throws ParseException {
        return new LexerImpl(new JsonLexerModule(json).build(), listener);
    }

    public Lexer fromJson(final String json) throws ParseException {
        return fromJson(json, new SymbolListenerImpl());
    }

    public Lexer fromJson(final InputStream inputStream, final SymbolListener listener) throws IOException, ParseException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromJson(writer.toString(), listener);
        }
    }

    public Lexer fromJson(final InputStream inputStream) throws IOException, ParseException {
        return fromJson(inputStream, new SymbolListenerImpl());
    }

    public Lexer fromJson(final File file, final SymbolListener listener) throws IOException, ParseException {
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromJson(inputStream, listener);
        }
    }

    public Lexer fromJson(final File file) throws IOException, ParseException {
        return fromJson(file, new SymbolListenerImpl());
    }

    public Lexer withModule(final AbstractLexerModule module, final SymbolListener listener) {
        return new LexerImpl(module.build(), listener);
    }

    public Lexer withModule(final AbstractLexerModule module) {
        return withModule(module, new SymbolListenerImpl());
    }
}
