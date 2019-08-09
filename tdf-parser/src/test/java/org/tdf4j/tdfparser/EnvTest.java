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

import org.tdf4j.core.model.Environment;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvTest extends TdfParserTest {

    @Test
    public void test() {
        final Interpreter interpreter = generate("EnvTest.tdf");
        final Environment environment = interpreter.getParserModule().build().getEnvironment();

        assertEquals(2, environment.getPackages().length);
        assertEquals("org.tdf4j.core.model.Token", environment.getPackages()[0]);
        assertEquals("org.tdf4j.core.module.LexerAbstractModule", environment.getPackages()[1]);
        assertEquals(0, environment.getDependencies().length);
        assertEquals("" +
                "        public String test() {" +
                "            return \"\";" +
                "        }" +
                "    ", environment.getCode().replaceAll("[\r\n]", ""));
    }

}
