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
package org.tdf4j.tdfparser;

import org.tdf4j.generator.Options;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.parser.ParserMetaInformation;

class TdfParserGenerator {

    public static void main(String[] args){
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
        TdfParserUtils.createClass(dir, name, tdfParser.getSourceCode());
    }

}
