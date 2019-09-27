package org.tdf4j.cli;

import com.beust.jcommander.JCommander;
import org.tdf4j.core.module.LexerAbstractModule;
import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.generator.Options;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.parser.Parser;
import org.tdf4j.tdfparser.*;

import org.tdf4j.tdfparser.impl.TdfInterpreter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GeneratorFromTdf {
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public static void main(String ... args) throws IOException {
        final Args programAgrs = new Args();
        JCommander.newBuilder()
                .addObject(programAgrs)
                .build()
                .parse(args);
        process(programAgrs);
    }

    private static void process(final Args args) throws IOException {
        final Interpreter interpreter = new TdfInterpreter();
        interpreter.parse(loadGrammar(args.grammar));
        generateParser(args, interpreter.getLexerModule(), interpreter.getParserModule());
    }

    private static void generateParser(final Args args, final LexerAbstractModule lexerModule, final ParserAbstractModule parserModule) {
        final Parser parser = new ParserGenerator(new Options.Builder()
                .setParserModule(parserModule)
                .setLexerModule(lexerModule)
                .setClassName(args.name)
                .setPackage(args.pack)
                .build()
        ).generate();
        TdfParserUtils.createClass(args.dir, args.name, parser.meta().getSourceCode());
    }

    private static String loadGrammar(final String fileName) throws IOException {
        try (
                final StringWriter writer = new StringWriter();
                final Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(fileName)), ENCODING)
        ) {
            int bt;
            while ((bt = reader.read()) != -1) {
                writer.write(bt);
            }
            return writer.toString();
        }
    }
}
