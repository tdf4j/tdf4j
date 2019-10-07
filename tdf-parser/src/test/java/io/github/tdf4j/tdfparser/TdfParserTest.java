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


import io.github.tdf4j.tdfparser.impl.TdfInterpreter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

class TdfParserTest {

    Interpreter generate(final String fileName) {
        final Interpreter interpreter = new TdfInterpreter();
        interpreter.parse(load(fileName));
        return interpreter;
    }

    @SuppressWarnings("ConstantConditions")
    String load(final String fileName) {
        try(final InputStream inputStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
            final StringWriter writer = new StringWriter())
        {
            int bt = 0;
            while((bt = inputStream.read()) != -1) {
                writer.write(bt);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
