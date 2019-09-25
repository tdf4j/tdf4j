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

import org.tdf4j.tdfparser.processor.StringProcessor;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringProcessorTest {
    private final StringProcessor stringProcessor = new StringProcessor();

    @Test
    public void normal() {
        assertEquals("abc", stringProcessor.process("\"abc\""));
        assertEquals("a\"bc\"", stringProcessor.process("\"a\\\"bc\\\"\""));
    }

    @Test
    public void not_string() {
        assertEquals("a\\\"bc\\\"", stringProcessor.process("a\\\"bc\\\""));
        assertEquals("abc", stringProcessor.process("abc"));
    }

    @Test
    public void empty_string() {
        assertEquals("", stringProcessor.process("\"\""));
    }

}
