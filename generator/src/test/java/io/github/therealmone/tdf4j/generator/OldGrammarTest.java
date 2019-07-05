package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.UnexpectedSymbolException;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class OldGrammarTest extends ParserTest {

    @BeforeClass
    public static void init() {
        lexer = new LexerGenerator(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("VAR").pattern("^[a-z]+$");
                tokenize("STRING").pattern("^\"[^\"]*\"$");
                tokenize("NEW").pattern("^new$").priority(1);
                tokenize("TYPEOF").pattern("^typeof$").priority(1);
                tokenize("HASHSET").pattern("^hashset$").priority(1);
                tokenize("ARRAYLIST").pattern("^arraylist$").priority(1);
                tokenize("GET").pattern("^get$").priority(1);
                tokenize("SIZE").pattern("^size$").priority(1);
                tokenize("PUT").pattern("^put$").priority(1);
                tokenize("REMOVE").pattern("^remove$").priority(1);
                tokenize("REWRITE").pattern("^rewrite$").priority(1);
                tokenize("PRINT").pattern("^print$").priority(1);
                tokenize("COMMA").pattern("^,$");
                tokenize("CONCAT").pattern("^\\+\\+$").priority(2);
                tokenize("QUOTE").pattern("^\"$");
                tokenize("DIGIT").pattern("^-?(0|([1-9][0-9]*))$").priority(1);
                tokenize("DOUBLE").pattern("^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$");
                tokenize("ASSIGN_OP").pattern("^=$");
                tokenize("OP").pattern("^[\\+\\-\\/\\*]|(div)|(mod)$").priority(1);
                tokenize("DEL").pattern("^;$", Pattern.MULTILINE);
                tokenize("WHILE").pattern("^while$").priority(1);
                tokenize("IF").pattern("^if$").priority(1);
                tokenize("ELSE").pattern("^else$").priority(1);
                tokenize("DO").pattern("^do$").priority(1);
                tokenize("FOR").pattern("^for$").priority(1);
                tokenize("LOP").pattern("^[&\\|\\^\\!]$").priority(1);
                tokenize("COP").pattern("^[<>]|(<=)|(>=)|(==)|(!=)$").priority(1);
                tokenize("LB").pattern("^\\($");
                tokenize("RB").pattern("^\\)$");
                tokenize("FLB").pattern("^\\{$");
                tokenize("FRB").pattern("^\\}$");
                tokenize("$").pattern("^\\$$");
                tokenize("ws").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
            }
        }).generate();
    }

    private final Parser parser = generate(new AbstractParserModule() {
        @Override
        public void configure() {
            prod("lang")
                    .then(repeat(nt("expr")))
                    .then(t("$"));

            prod("expr")
                    .then(oneOf(
                            nt("while_loop"),
                            nt("for_loop"),
                            nt("if_statement"),
                            nt("do_while_loop"),
                            nt("print_expr"),
                            nt("assign_expr"),
                            nt("init_expr"),
                            nt("put_expr"),
                            nt("remove_expr"),
                            nt("rewrite_expr")
                    ));

            prod("while_loop")
                    .is(
                            t("WHILE"),
                            t("LB"),
                            nt("condition"),
                            t("RB"),
                            t("FLB"),
                            repeat(nt("expr")),
                            t("FRB")
                    );

            prod("for_loop")
                    .is(
                            t("FOR"),
                            t("LB"),
                            nt("assign_expr_without_del"),
                            t("DEL"),
                            nt("condition"),
                            t("DEL"),
                            nt("assign_expr_without_del"),
                            t("RB"),
                            t("FLB"),
                            repeat(nt("expr")),
                            t("FRB")
                    );

            prod("if_statement")
                    .is(
                            t("IF"),
                            t("LB"),
                            nt("condition"),
                            t("RB"),
                            t("FLB"),
                            repeat(nt("expr")),
                            t("FRB"),
                            optional(nt("else_stmt"))
                    );

            prod("else_stmt")
                    .is(
                            t("ELSE"),
                            t("FLB"),
                            repeat(nt("expr")),
                            t("FRB")
                    );

            prod("assign_expr")
                    .is(
                            t("VAR"),
                            t("ASSIGN_OP"),
                            nt("value_expr"),
                            t("DEL")
                    );

            prod("assign_expr_without_del")
                    .is(
                            t("VAR"),
                            t("ASSIGN_OP"),
                            nt("value_expr")
                    );

            prod("do_while_loop")
                    .is(
                            t("DO"),
                            t("FLB"),
                            repeat(nt("expr")),
                            t("FRB"),
                            t("WHILE"),
                            t("LB"),
                            nt("condition"),
                            t("RB")
                    );

            prod("print_expr")
                    .is(
                            t("PRINT"),
                            t("LB"),
                            nt("print_parameters"),
                            t("RB"),
                            t("DEL")
                    );

            prod("print_parameters")
                    .is(
                            nt("value_expr"),
                            repeat(
                                    t("CONCAT"),
                                    nt("print_parameters")
                            )
                    );

            prod("put_expr")
                    .is(
                            t("PUT"),
                            t("LB"),
                            t("VAR"),
                            t("COMMA"),
                            nt("value"),
                            t("RB"),
                            t("DEL")
                    );

            prod("remove_expr")
                    .is(
                            t("REMOVE"),
                            t("LB"),
                            t("VAR"),
                            t("COMMA"),
                            nt("value"),
                            t("RB"),
                            t("DEL")
                    );

            prod("rewrite_expr")
                    .is(
                            t("REWRITE"),
                            t("LB"),
                            t("VAR"),
                            t("COMMA"),
                            nt("value"),
                            t("COMMA"),
                            nt("value_expr"),
                            t("RB"),
                            t("DEL")
                    );

            prod("init_expr")
                    .is(
                            t("NEW"),
                            t("VAR"),
                            optional(nt("inst_expr")),
                            t("DEL")
                    );

            prod("inst_expr")
                    .is(
                            or(
                                    group(t("ASSIGN_OP"), nt("value_expr")),
                                    group(t("TYPEOF"), nt("type"))
                            )
                    );

            prod("type")
                    .is(oneOf(
                            t("ARRAYLIST"),
                            t("HASHSET")
                    ));

            prod("condition")
                    .is(
                            or(
                                    group(t("LB"), nt("condition"), t("RB")),
                                    nt("compare_expr")
                            ),
                            repeat(
                                    t("LOP"),
                                    nt("condition")
                            )
                    );

            prod("compare_expr")
                    .is(
                            nt("value_expr"),
                            t("COP"),
                            nt("value_expr")
                    );

            prod("value_expr")
                    .is(
                            or(
                                    group(t("LB"), nt("value_expr"),  t("RB")),
                                    nt("value_expr_1")
                            ),
                            repeat(
                                    t("OP"),
                                    nt("value_expr")
                            )
                    );

            prod("value_expr_1")
                    .is(
                            oneOf(
                                nt("value"),
                                nt("get_expr"),
                                nt("size_expr")
                            ),
                            repeat(t("OP"), nt("value_expr"))
                    );

            prod("value")
                    .is(oneOf(
                            t("VAR"),
                            t("DIGIT"),
                            t("DOUBLE"),
                            t("STRING")
                    ));

            prod("get_expr")
                    .is(
                            t("GET"),
                            t("LB"),
                            t("VAR"),
                            t("COMMA"),
                            nt("value"),
                            t("RB")
                    );

            prod("size_expr")
                    .is(
                            t("SIZE"),
                            t("LB"),
                            t("VAR"),
                            t("RB")
                    );
        }
    });

    @Test
    public void Tests_ParserTest_parserTest() {
        assertNotNull(parse(parser, "while(a > b) {}$"));
        assertNotNull(parse(parser, "while((a > b) & (c < d)) {}$"));
        assertNotNull(parse(parser, "while((((((a > b))))) & (((((c < d)))))) {}$"));
        assertNotNull(parse(parser, "while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$"));
        assertNotNull(parse(parser, "while(((((((((((c < d))))))))))) {}$"));
        assertNotNull(parse(parser, "while(a < b & c > d) {}$"));
        assertNotNull(parse(parser, "while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$"));
        assertNotNull(parse(parser, "if(a > b) {}$"));
        assertNotNull(parse(parser, "if((a > b) & (c < d)) {}$"));
        assertNotNull(parse(parser, "if((((((a > b))))) & (((((c < d)))))) {}$"));
        assertNotNull(parse(parser, "if((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300))){}$"));
        assertNotNull(parse(parser, "if(((((((((((c < d))))))))))) {}$"));
        assertNotNull(parse(parser, "if(a < b & c > d) {}$"));
        assertNotNull(parse(parser, "if(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300){}$"));
        assertNotNull(parse(parser, "for(i = 0; i < 100; i = i + 1) {}$"));
        assertNotNull(parse(parser, "for(i = (((((a + b))))) * (((((c + d))))); i > 100; i = i + 1){}$"));
        assertNotNull(parse(parser, "for(i = (((1 + 1) * (1 + 2)) * (1 + 1)) * (100 - 10); i > 100; i = i + 1){}$"));
        assertNotNull(parse(parser, "do{}while(a > b)$"));
        assertNotNull(parse(parser, "do{}while((a > b) & (c < d))$"));
        assertNotNull(parse(parser, "do{}while((((((a > b))))) & (((((c < d))))))$"));
        assertNotNull(parse(parser, "do{}while((a == b) & (c < d * (5 + 1)) | ((10 *(s + 150) <= 300)))$"));
        assertNotNull(parse(parser, "do{}while(((((((((((c < d)))))))))))$"));
        assertNotNull(parse(parser, "do{}while(a < b & c > d)$"));
        assertNotNull(parse(parser, "do{}while(a == b & c < d * (5 + 1) | 10 * (s + 150) <= 300)$"));
        assertNotNull(parse(parser, "print(0);$"));
        assertNotNull(parse(parser, "new a = 100; print(a);$"));
        assertNotNull(parse(parser, "new a typeof arraylist; put(a, 100); print(get(a, 0));$"));
        assertNotNull(parse(parser, "new a typeof hashset; new i = 0; put(a, i); print(get(a, i));$"));
        assertNotNull(parse(parser, "new a typeof hashset; new i = 100; put(a, i); i = get(a, i);$"));
        assertNotNull(parse(parser, "new a typeof arraylist; put(a, 100); i = get(a, 0);$"));
        assertNotNull(parse(parser, "new a typeof hashset; new i = 100; put(a, i); remove(a, i);$"));
        assertNotNull(parse(parser, "new a typeof arraylist; put(a, 100); remove(a, 0);$"));
        assertNotNull(parse(parser, "while(a > b) {if (a < d) {do{i = i;}while(a < b)} else {for(i = i; i < 100; i = i + 1){i = i;}}}$"));
        assertNotNull(parse(parser, "a = b; c = a; a = a;$"));
    }

    @Test
    public void Tests_ParserTest_converterTests() {
        assertNotNull(parse(parser, "$"));
        assertNotNull(parse(parser, "while(a < b) {a = a + 1;}$"));
        assertNotNull(parse(parser, "while(a < b & c > d) {}$"));
        assertNotNull(parse(parser, "while(((a < b) & (c > d)) | ((a > c) & (b < d))) {}$"));
        assertNotNull(parse(parser, "while((a < b) & (c > d) | (a > c) & (b < d)) {}$"));
        assertNotNull(parse(parser, "while(a < b) {if (a < b) {}}$"));
        assertNotNull(parse(parser, "while(a < b) {do{} while(a < b)}$"));
        assertNotNull(parse(parser, "while(a < b) {for(i = 1; i < 100; i = i + 1) {}}$"));
        assertNotNull(parse(parser, "while(a < b) {new a typeof hashset; new i = 1; put(a, i);}$"));
        assertNotNull(parse(parser, "for(i = 1; (i < n) & (n > i); i = i + 1) {}$"));
    }

    @Test
    public void Tests_ParserTest_testOptimizer() {
        assertNotNull(parse(parser, "print(100 / (25 + 25));$"));
        assertNotNull(parse(parser, "print(1 / (100 * (50 - (1 / 0.16))));$"));
        assertNotNull(parse(parser, "print(100 / (25 + 25 - a));$"));
        assertNotNull(parse(parser, "print(1 / (100 * (50 - (1 / 0.16 - a))));$"));
    }

    @Test
    public void Tests_StackMachineTest_collectionsTest() {
        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "new one = 1;" +
                        "new two = 2;" +
                        "put(a, one);" +
                        "put(a, two);" +
                        "print(get(a, one));" +
                        "print(get(a, two));" +
                        "remove(a, one);" +
                        "print(get(a, one));$"));

        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "put(a, i);$"));

        assertNotNull(parse(parser,
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

        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "new i = get(a, 100);$"));

        assertNotNull(parse(parser,
                "new a typeof arraylist;" +
                        "new i = get(a, i);$"));
    }

    @Test
    public void Tests_StackMachineTest_printTest() {
        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(get(a, i));$"));

        assertNotNull(parse(parser,
                "new a typeof arraylist;" +
                        "put(a, 100);" +
                        "print(get(a, 0));$"));

        assertNotNull(parse(parser,
                "print(\"Test String\");$"));

        assertNotNull(parse(parser,
                "new i = 100;" +
                        "print(\"This value equals \" ++ i);$"));

        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "new i = 100;" +
                        "put(a, i);" +
                        "print(\"This value equals \" ++ i ++ \": \" ++ get(a, i));$"));

        assertNotNull(parse(parser,
                "new a typeof hashset;" +
                        "print(a);$"));
    }

    @Test
    public void Tests_StackMachineTest_mathOperationsTest() {
        assertNotNull(parse(parser, "print(100 + 100);$"));
        assertNotNull(parse(parser, "print(100 * 10);$"));
        assertNotNull(parse(parser, "print(100 - 101);$"));
        assertNotNull(parse(parser, "print(100 - 101);$"));
        assertNotNull(parse(parser, "print(100 / 5 * -1);$"));
        assertNotNull(parse(parser, "print(100 div 10);$"));
        assertNotNull(parse(parser, "print(105 mod 20);$"));
        assertNotNull(parse(parser, "print(2 * 2 + 2);$"));
        assertNotNull(parse(parser, "print(2 * (2 + 2));$"));
    }

    @Test
    public void Tests_StackMachineTest_typeCastTest() {
        assertNotNull(parse(parser,
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

        assertNotNull(parse(parser,
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
        assertParserFails(parser, "new a = hashset;$", "Unexpected token: Token{tag=HASHSET, value=hashset, row=1, column=8}. Expected: [LB, value_expr_1]");
        assertParserFails(parser, "get(a, 1);$", "Unexpected token: Token{tag=GET, value=get, row=1, column=0}. Expected: [$]");
        assertParserFails(parser, "while(a & b);$", "Unexpected token: Token{tag=LOP, value=&, row=1, column=8}. Expected: [COP]");
        assertThrows(() -> parse(parser, "@$"), UnexpectedSymbolException.class, "Unexpected symbol: @ ( line 1, column 1 )");
    }

}
