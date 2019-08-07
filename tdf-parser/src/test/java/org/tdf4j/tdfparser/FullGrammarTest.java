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

package org.tdf4j.tdfparser;


import org.tdf4j.generator.LexerOptions;
import org.tdf4j.generator.ParserOptions;
import org.tdf4j.generator.impl.LexerGenerator;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.lexer.UnexpectedSymbolException;
import org.tdf4j.model.ast.AST;
import org.tdf4j.parser.Parser;
import org.tdf4j.parser.UnexpectedTokenException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class FullGrammarTest extends TdfParserTest {
    Lexer lexer;
    Parser parser;

    @Before
    public void before() {
        final Interpreter interpreter = generate("FullGrammarTest.tdf");
        System.out.println(interpreter.getParserModule().build().getGrammar());
        this.lexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(interpreter.getLexerModule())
                .build()
        ).generate();
        this.parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("FullGrammarTest_parser")
                .setModule(interpreter.getParserModule())
                .build()
        ).generate();
    }

    @Test
    public void Tests_ParserTest_parserTest() {
        assertNotNull(parse("while(a > b) {}$"));
        assertNotNull(parse("while((a > b) & (c < d)) {}$"));
        assertNotNull(parse("while((((((a > b))))) & (((((c < d)))))) {}$"));
        assertNotNull(parse("while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$"));
        assertNotNull(parse("while(((((((((((c < d))))))))))) {}$"));
        assertNotNull(parse("while(a < b & c > d) {}$"));
        assertNotNull(parse("while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$"));
        assertNotNull(parse("if(a > b) {}$"));
        assertNotNull(parse("if((a > b) & (c < d)) {}$"));
        assertNotNull(parse("if((((((a > b))))) & (((((c < d)))))) {}$"));
        assertNotNull(parse("if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$"));
        assertNotNull(parse("if(((((((((((c < d))))))))))) {}$"));
        assertNotNull(parse("if(a < b & c > d) {}$"));
        assertNotNull(parse("if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$"));
        assertNotNull(parse("for(i = 0; i < 100; i = i + 1) {}$"));
        assertNotNull(parse("for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}$"));
        assertNotNull(parse("for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}$"));
        assertNotNull(parse("do{}while(a > b)$"));
        assertNotNull(parse("do{}while((a > b) & (c < d))$"));
        assertNotNull(parse("do{}while((((((a > b))))) & (((((c < d))))))$"));
        assertNotNull(parse("do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))$"));
        assertNotNull(parse("do{}while(((((((((((c < d)))))))))))$"));
        assertNotNull(parse("do{}while(a < b & c > d)$"));
        assertNotNull(parse("do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)$"));
        assertNotNull(parse("print(0);$"));
        assertNotNull(parse("new a = 100; print(a);$"));
        assertNotNull(parse("new a typeof arraylist; put(a, 100); print(get(a, 0));$"));
        assertNotNull(parse("new a typeof hashset; new i = 0; put(a, i); print(get(a, i));$"));
        assertNotNull(parse("new a typeof hashset; new i = 100; put(a, i); i = get(a, i);$"));
        assertNotNull(parse("new a typeof arraylist; put(a, 100); i = get(a, 0);$"));
        assertNotNull(parse("new a typeof hashset; new i = 100; put(a, i); remove(a, i);$"));
        assertNotNull(parse("new a typeof arraylist; put(a, 100); remove(a, 0);$"));
        assertNotNull(parse("while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}$"));
        assertNotNull(parse("a = b; c = a; a = a;$"));
    }

    @Test
    public void Tests_ParserTest_converterTests() {
        assertNotNull(parse("$"));
        assertNotNull(parse("while(a < b) {a = a + 1;}$"));
        assertNotNull(parse("while(a < b & c > d) {}$"));
        assertNotNull(parse("while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}$"));
        assertNotNull(parse("while((a < b) & (c > d) | (a > c) & (b < d)) {}$"));
        assertNotNull(parse("while(a < b) {if (a < b) {}}$"));
        assertNotNull(parse("while(a < b) {do{} while(a < b)}$"));
        assertNotNull(parse("while(a < b) {for(i = 1; i < 100; i = i + 1) {}}$"));
        assertNotNull(parse("while(a < b) {new a typeof hashset; new i = 1; put(a, i);}$"));
        assertNotNull(parse("for(i = 1; (i < n) & (n > i); i = i + 1) {}$"));
    }

    @Test
    public void Tests_ParserTest_testOptimizer() {
        assertNotNull(parse("print(100 / (25 + 25));$"));
        assertNotNull(parse("print(1 / (100 * (50 - (1 / 0.16))));$"));
        assertNotNull(parse("print(100 / (25 + 25 - a));$"));
        assertNotNull(parse("print(1 / (100 * (50 - (1 / 0.16 - a))));$"));
    }

    @Test
    public void Tests_StackMachineTest_collectionsTest() {
        assertNotNull(parse(
                "new a typeof hashset;" +
                        "new one = 1;" +
                        "new two = 2;" +
                        "put(a, one);" +
                        "put(a, two);" +
                        "print(get(a, one));" +
                        "print(get(a, two));" +
                        "remove(a, one);" +
                        "print(get(a, one));$"));

        assertNotNull(parse(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "put(a, i);$"));

        assertNotNull(parse(
                "new a typeof arraylist;" +
                        "new i = 0;" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   put(a, i);" +
                        "}" +
                        "" +
                        "for(i = 0; i < 5; i = i + 1) {" +
                        "   print(get(a, i));" +
                        "}" +
                        "print(get(a, -1));$"));

        assertNotNull(parse(
                "new a typeof hashset;" +
                        "new i = get(a, 100);$"));

        assertNotNull(parse(
                "new a typeof arraylist;" +
                        "new i = get(a, i);$"));
    }

    @Test
    public void Tests_StackMachineTest_printTest() {
        assertNotNull(parse(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(get(a, i));$"));

        assertNotNull(parse(
                "new a typeof arraylist;" +
                        "put(a, 100);" +
                        "print(get(a, 0));$"));

        assertNotNull(parse(
                "print(\"Test String\");$"));

        assertNotNull(parse(
                "new i = 100;" +
                        "print(\"This value equals \" ++ i);$"));

        assertNotNull(parse(
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(\"This value equals \" ++ i ++ \": \" ++ get(a, i));$"));

        assertNotNull(parse(
                "new a typeof hashset;" +
                        "print(a);$"));
    }

    @Test
    public void Tests_StackMachineTest_mathOperationsTest() {
        assertNotNull(parse("print(100 + 100);$"));
        assertNotNull(parse("print(100 * 10);$"));
        assertNotNull(parse("print(100 - 101);$"));
        assertNotNull(parse("print(100 - 101);$"));
        assertNotNull(parse("print(100 / 5 * -1);$"));
        assertNotNull(parse("print(100 div 10);$"));
        assertNotNull(parse("print(105 mod 20);$"));
        assertNotNull(parse("print(2 * 2 + 2);$"));
        assertNotNull(parse("print(2 * (2 + 2));$"));
    }

    @Test
    public void Tests_StackMachineTest_typeCastTest() {
        assertNotNull(parse(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "a = \"Test\";" +
                        "print(a);" +
                        "a = 100;" +
                        "print(a);" +
                        "a = 101.101;" +
                        "print(a);$"));

        assertNotNull(parse(
                "new a typeof arraylist;" +
                        "put(a, 1);" +
                        "put(a, 2);" +
                        "print(a);" +
                        "new b typeof hashset;" +
                        "a = b;" +
                        "print(a);" +
                        "put(a, 1);$"));
    }

    @Test
    public void Tests_StackMachineTest_errorsTest() {
        assertParserFails("new a = hashset;$", "Unexpected token: Token{tag=HASHSET, value=hashset, row=1, column=8}. Expected: [LB, value_expr_1]");
        assertParserFails("get(a, 1);$", "Unexpected token: Token{tag=GET, value=get, row=1, column=0}. Expected: [EOF]");
        assertParserFails("while(a & b);$", "Unexpected token: Token{tag=LOP, value=&, row=1, column=8}. Expected: [COP]");
        assertThrows(() -> parse("@$"), UnexpectedSymbolException.class, "Unexpected symbol: @ ( line 1, column 1 )");
    }

    private void assertParserFails(final String input, final String message) {
        assertThrows(() -> parse(input), UnexpectedTokenException.class, message);
    }

    private void assertThrows(final Callback callback, final Class<? extends Throwable> ex, final String message) {
        try {
            callback.call();
            fail("Expected exception: " + ex.getName() + ": " + message);
        } catch (Exception e) {
            assertEquals(ex.getName(), e.getClass().getName());
            assertEquals(message, e.getMessage());
        }
    }

    @FunctionalInterface
    interface Callback {
        void call();
    }

    private AST parse(final String input) {
        final long current = System.currentTimeMillis();
        final AST ast = this.parser.parse(this.lexer.stream(input));
        System.out.println("Parsing time: " + (System.currentTimeMillis() - current));
        System.out.println(ast);
        return ast;
    }
}
