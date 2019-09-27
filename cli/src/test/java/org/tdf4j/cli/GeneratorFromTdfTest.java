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

package org.tdf4j.cli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class GeneratorFromTdfTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldGenerateWithAllArgs() throws IOException {
        final URL grammar = Thread.currentThread().getContextClassLoader().getResource("TdfGrammar.tdf");
        GeneratorFromTdf.main(
                "-g", grammar.getPath(),
                "-dir", folder.getRoot().getAbsolutePath(),
                "-p", "org.tdf4j.cli",
                "-cn", "TmpParser"
        );
        assertEquals(1, folder.getRoot().list().length);
        assertEquals("TmpParser.java", folder.getRoot().listFiles()[0].getName());
    }

}
