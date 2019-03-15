package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import io.github.therealmone.tdf4j.lexer.config.JsonLexerModule;
import io.github.therealmone.tdf4j.lexer.config.XmlLexerModule;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import org.json.simple.parser.ParseException;

import java.io.*;

public class LexerFactory{
    public Lexer fromXml(final String xml) {
        return new LexerImpl(new XmlLexerModule(xml).build());
    }

    public Lexer fromXml(final InputStream inputStream) throws IOException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromXml(writer.toString());
        }
    }

    public Lexer fromXml(final File file) throws IOException{
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromXml(inputStream);
        }
    }

    public Lexer fromJson(final String json) throws ParseException {
        return new LexerImpl(new JsonLexerModule(json).build());
    }

    public Lexer fromJson(final InputStream inputStream) throws IOException, ParseException {
        try(final StringWriter writer = new StringWriter()) {
            int bt;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return fromJson(writer.toString());
        }
    }

    public Lexer fromJson(final File file) throws IOException, ParseException {
        try(final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return fromJson(inputStream);
        }
    }

    public Lexer withModule(final AbstractLexerModule module) {
        return new LexerImpl(module.build());
    }
}
