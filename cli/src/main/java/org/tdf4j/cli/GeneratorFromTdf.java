package org.tdf4j.cli;

import com.beust.jcommander.JCommander;
import org.tdf4j.generator.Options;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.tdfparser.*;

import com.beust.jcommander.Parameter;
import org.tdf4j.tdfparser.impl.TdfInterpreter;

import java.io.*;

public class GeneratorFromTdf {

    // задаются как аргменты
    @Parameter(names = {"--fileName", "-fn"})
    private static String fileName;

    @Parameter(names = {"--directory", "-dir"})
    private static String dir;

    @Parameter(names = {"--package", "-p"})
    private static String pack;

    @Parameter(names = {"--className", "-cn"})
    private static String name;


    public static void main(String ... args) {
        final GeneratorFromTdf generator = new GeneratorFromTdf();
        JCommander.newBuilder()
                .addObject(generator)
                .build()
                .parse(args);
        generator.run();
    }

    private void run() {
        final Interpreter interpreter = new TdfInterpreter();
        interpreter.parse(load());

        final TdfParser tdfParser = (TdfParser) new ParserGenerator(new Options.Builder()
                .setParserModule(interpreter.getParserModule())
                .setLexerModule(interpreter.getLexerModule())
                .setClassName(name)
                .setPackage(pack)
                .build()
        ).generate();

        TdfParserUtils.createClass(dir, name, tdfParser.meta().getSourceCode());
    }

    @SuppressWarnings("ConstantConditions")
    private static String load() {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            try (final InputStream inputStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
                 final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
