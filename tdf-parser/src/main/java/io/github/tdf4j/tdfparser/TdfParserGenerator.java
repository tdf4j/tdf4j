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
package io.github.tdf4j.tdfparser;

import io.github.tdf4j.generator.Options;
import io.github.tdf4j.generator.impl.ParserGenerator;
import io.github.tdf4j.parser.ParserMetaInformation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class TdfParserGenerator {

    public static void main(String[] args) throws IOException {
        final String dir = args[0];
        final String pack = args[1];
        final String name = args[2];
        final ParserMetaInformation tdfParser = new ParserGenerator(new Options.Builder()
                .setInterface(TdfParser.class)
                .setParserModule(new TdfParserModule())
                .setLexerModule(new TdfLexerModule())
                .setClassName(name)
                .setPackage(pack)
                .build()
        ).generate();
        createClass(dir, name, tdfParser.getSourceCode());
    }

    private static void createClass(final String dir, final String fileName, final String code) throws IOException {
        final File file = new File(dir, fileName + ".java");
        if(file.exists()) {
            throw new IllegalArgumentException("File '" + file.getName() + "' already exists");
        }
        if(!file.createNewFile()) {
            throw new IOException("Can't create new file '" + file.getName() + "'");
        }
        try(final FileWriter writer = new FileWriter(file)) {
            writer.write(code);
            writer.flush();
        }
    }

}
