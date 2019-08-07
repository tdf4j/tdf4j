/*
 * Copyright (c) 2019 Roman Fatnev
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

package org.tdf4j.generator;

import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.parser.Parser;
import org.tdf4j.parser.UnexpectedTokenException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnexpectedTokensTest extends ParserTest {

    @Test
    public void terminals() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(t("A"), t("B"), t("C"));
            }
        });
        try {
            parse(parser, "ACB");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=1}. Expected: [B]", e.getMessage());
        }
    }

    @Test
    public void or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(or(t("A"), t("B")));
            }
        });
        try {
            parse(parser, "C");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=0}. Expected: [A, B]", e.getMessage());
        }
    }

    @Test
    public void or_of_non_terminals() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(or(nt("a"), nt("b")));
                prod("a").is(t("A"));
                prod("b").is(t("B"));
            }
        });
        try {
            parse(parser, "C");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=0}. Expected: [a, b]", e.getMessage());
        }
    }

}
