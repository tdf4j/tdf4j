/*
 *
 *  Copyright (c) 2019 tdf4j
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.tdf4j.tdfparser.impl;

import org.tdf4j.parser.*;
import org.tdf4j.core.model.*;
import org.tdf4j.core.model.ast.*;
import org.tdf4j.core.model.ebnf.*;
import org.tdf4j.core.utils.*;
import java.util.*;
import java.util.function.*;
import static org.tdf4j.core.model.ast.ASTCursor.Movement.*;
import org.tdf4j.tdfparser.TdfParser;
import org.tdf4j.core.module.LexerAbstractModule;
import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.tdfparser.constructor.*;
import org.tdf4j.tdfparser.processor.*;

public class TdfParserImpl implements TdfParser {
    private final MetaInf meta;
    private final Predictor predictor;


    private AST ast;
    private BufferedStream<Token> stream;

    public TdfParserImpl(
        final MetaInf meta,
        final Predictor predictor
    ) {
        this.meta = meta;
        this.predictor = predictor;
    }

    private final Processor<String> stringProcessor = new StringProcessor();
    private final Stack<LetterConstructor> letters = new Stack<>();
    private final Stack<EnvironmentConstructor> environments = new Stack<>();
    private final Stack<ProductionConstructor> productions = new Stack<>();
    private LexerAbstractModule lexerModule;
    private org.tdf4j.core.module.ParserAbstractModule parserModule;

    @Override
    public LexerAbstractModule getLexerModule() {
       return this.lexerModule;
    }

    @Override
    public ParserAbstractModule getParserModule() {
       return this.parserModule;
    }


    @Override
    public AST parse(final Stream<Token> tokens) {
        this.stream = new BufferedStream<>(tokens);
        this.ast = AST.create("tdf_lang");
        tdf_lang();
        return ast;
    }

    @Override
    public AST parse(final List<Token> tokens) {
        return parse(Stream.of(tokens));
    }

    @Override
    public MetaInf meta() {
        return this.meta;
    }

    private boolean canReach(final String element) {
        return predictor.predict(stream.peek()).contains(element);
    }

    private boolean canReachAny(final String... elements) {
        for(final String element : elements) {
            if(canReach(element)) {
                return true;
            }
        }
        return false;
    }

    private void match(final String terminal) {
        match(terminal, null);
    }

    private void match(final String terminal, final Consumer<Token> action) {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase(terminal)) {
            ast.addLeaf(stream.next());
            if(action != null) {
                action.accept(ast.moveCursor(TO_LAST_LEAF_CHILD).onCursor().asLeaf().getToken());
                ast.moveCursor(TO_PARENT);
            }
        } else {
            throw new UnexpectedTokenException(stream.peek(), terminal);
        }
    }

    private int predict(final Alt... alternatives) throws UnexpectedTokenException {
        final java.util.Optional<Alt> choice = Arrays.stream(alternatives)
            .filter(alt -> canReachAny(alt.elements))
            .findFirst();
        if(!choice.isPresent()) {
            throw new UnexpectedTokenException(stream.peek(), expected(alternatives));
        }
        return choice.get().index;
    }

    private String[] expected(final Alt... alternatives) {
        return Arrays.stream(alternatives)
            .flatMap(alt -> Arrays.stream(alt.elements))
            .toArray(String[]::new);
    }

    private class Alt {
        final String[] elements;
        final int index;
        public Alt(final int index, final String... elements) {
            this.elements = elements;
            this.index = index;
        }
    }

    private void ebnf_optional() {
        match("LEFT_SQUARE_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_elements_set();
        ast.moveCursor(TO_PARENT);
        match("RIGHT_SQUARE_BRACKET");
    }

    private void ebnf_elements_set() {
        ast.addNode("ebnf_element").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_element();
        ast.moveCursor(TO_PARENT);
        if(canReach("COMMA")) {
            match("COMMA");
            ast.addNode("ebnf_elements_set").moveCursor(TO_LAST_NODE_CHILD);
            ebnf_elements_set();
            ast.moveCursor(TO_PARENT);
        }
    }

    private void ebnf_or() {
        for(int i487805674 = 0; i487805674 < 2; i487805674++) {
            match("LOP_OR");
            ast.addNode("ebnf_element").moveCursor(TO_LAST_NODE_CHILD);
            ebnf_element();
            ast.moveCursor(TO_PARENT);
        }
        while(true) {
            if(canReach("LOP_OR")) {
                match("LOP_OR");
                ast.addNode("ebnf_element").moveCursor(TO_LAST_NODE_CHILD);
                ebnf_element();
                ast.moveCursor(TO_PARENT);
            } else {
                break;
            }
        }
    }

    private void tdf_lang() {
        this.lexerModule = new LexerAbstractModule() {
           @Override
           public void configure() {}
        };

        this.parserModule = new ParserAbstractModule() {
           @Override
           public void configure() {}
        };

        ast.addNode("lexis").moveCursor(TO_LAST_NODE_CHILD);
        lexis();
        ast.moveCursor(TO_PARENT);
        if(canReach("environment")) {
            ast.addNode("environment").moveCursor(TO_LAST_NODE_CHILD);
            environment();
            ast.moveCursor(TO_PARENT);
        }
        ast.addNode("syntax").moveCursor(TO_LAST_NODE_CHILD);
        syntax();
        ast.moveCursor(TO_PARENT);
        match("EOF");
    }

    private void lexis() {
        match("KEY_LEXIS");
        while(true) {
            if(canReach("terminal_description")) {
                ast.addNode("terminal_description").moveCursor(TO_LAST_NODE_CHILD);
                terminal_description();
                ast.moveCursor(TO_PARENT);
            } else {
                break;
            }
        }
    }

    private void env_import() {
        match("KEY_IMPORT");
        match("STRING", token -> {
            environments.peek().addPackage(stringProcessor.process(token.getValue()));
        });
    }

    private void terminal_parameter_pattern_flag() {
        match("TERMINAL_PARAMETER_PATTERN_FLAG");
        match("COLON");
        ast.addNode("pattern_flags").moveCursor(TO_LAST_NODE_CHILD);
        pattern_flags();
        ast.moveCursor(TO_PARENT);
    }

    private void terminal_parameter_priority() {
        match("TERMINAL_PARAMETER_PRIORITY");
        match("COLON");
        match("INTEGER", token -> {
            letters.peek().setPriority(token.getValue());
        });
    }

    private void production_description() {
        match("NON_TERMINAL", token -> {
            productions.push(new ProductionConstructor(parserModule.prod(token.getValue())));
        });
        match("OP_ASSIGN");
        ast.addNode("ebnf_elements_set").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_elements_set();
        ((Consumer<ASTNode>) node -> {
            productions.peek().setElements(node);
        }).accept(ast.onCursor().asNode());
        ast.moveCursor(TO_PARENT);
        match("DELIMITER");
        productions.pop().construct();

    }

    private void ebnf_terminal() {
        match("TERMINAL_TAG");
        if(canReach("LAMBDA")) {
            match("LAMBDA");
            match("STRING");
        }
    }

    private void pattern_flags() {
        switch(predict(
                new Alt(0, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES"),
                new Alt(1, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE"),
                new Alt(2, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS"),
                new Alt(3, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE"),
                new Alt(4, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL"),
                new Alt(5, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL"),
                new Alt(6, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE"),
                new Alt(7, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ"),
                new Alt(8, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS"))
        ) {
            case 0 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES");
                    break;
                }
            case 1 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE");
                    break;
                }
            case 2 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS");
                    break;
                }
            case 3 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE");
                    break;
                }
            case 4 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL");
                    break;
                }
            case 5 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL");
                    break;
                }
            case 6 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE");
                    break;
                }
            case 7 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ");
                    break;
                }
            case 8 : {
                    match("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                    break;
                }
        }
        letters.peek().addFlag(ast.getLastLeaf().getToken().getValue());

        if(canReach("OP_SUM")) {
            match("OP_SUM");
            ast.addNode("pattern_flags").moveCursor(TO_LAST_NODE_CHILD);
            pattern_flags();
            ast.moveCursor(TO_PARENT);
        }
    }

    private void environment() {
        match("KEY_ENV");
        environments.push(new EnvironmentConstructor(parserModule.environment()));

        while(true) {
            if(canReach("env_import")) {
                ast.addNode("env_import").moveCursor(TO_LAST_NODE_CHILD);
                env_import();
                ast.moveCursor(TO_PARENT);
            } else {
                break;
            }
        }
        if(canReach("env_code")) {
            ast.addNode("env_code").moveCursor(TO_LAST_NODE_CHILD);
            env_code();
            ast.moveCursor(TO_PARENT);
        }
        environments.pop().construct();

    }

    private void ebnf_non_terminal() {
        match("NON_TERMINAL");
        if(canReach("LAMBDA")) {
            match("LAMBDA");
            match("STRING");
        }
    }

    private void terminal_parameters_values() {
        switch(predict(
                new Alt(0, "terminal_parameter_priority"),
                new Alt(1, "terminal_parameter_hidden"),
                new Alt(2, "terminal_parameter_pattern_flag"))
        ) {
            case 0 : {
                    ast.addNode("terminal_parameter_priority").moveCursor(TO_LAST_NODE_CHILD);
                    terminal_parameter_priority();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 1 : {
                    ast.addNode("terminal_parameter_hidden").moveCursor(TO_LAST_NODE_CHILD);
                    terminal_parameter_hidden();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 2 : {
                    ast.addNode("terminal_parameter_pattern_flag").moveCursor(TO_LAST_NODE_CHILD);
                    terminal_parameter_pattern_flag();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
        }
        if(canReach("COMMA")) {
            match("COMMA");
            ast.addNode("terminal_parameters_values").moveCursor(TO_LAST_NODE_CHILD);
            terminal_parameters_values();
            ast.moveCursor(TO_PARENT);
        }
    }

    private void ebnf_repetition() {
        match("INTEGER");
        match("OP_MULTIPLY");
        ast.addNode("ebnf_element").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_element();
        ast.moveCursor(TO_PARENT);
    }

    private void ebnf_group() {
        match("LEFT_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_elements_set();
        ast.moveCursor(TO_PARENT);
        match("RIGHT_BRACKET");
    }

    private void terminal_parameters() {
        match("LEFT_SQUARE_BRACKET");
        ast.addNode("terminal_parameters_values").moveCursor(TO_LAST_NODE_CHILD);
        terminal_parameters_values();
        ast.moveCursor(TO_PARENT);
        match("RIGHT_SQUARE_BRACKET");
    }

    private void env_code() {
        match("KEY_CODE");
        match("STRING", token -> {
            environments.peek().setCode(stringProcessor.process(token.getValue()));
        });
    }

    private void ebnf_repeat() {
        match("LEFT_FIGURE_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(TO_LAST_NODE_CHILD);
        ebnf_elements_set();
        ast.moveCursor(TO_PARENT);
        match("RIGHT_FIGURE_BRACKET");
    }

    private void syntax() {
        match("KEY_SYNTAX");
        while(true) {
            if(canReach("production_description")) {
                ast.addNode("production_description").moveCursor(TO_LAST_NODE_CHILD);
                production_description();
                ast.moveCursor(TO_PARENT);
            } else {
                break;
            }
        }
    }

    private void ebnf_element() {
        switch(predict(
                new Alt(0, "ebnf_optional"),
                new Alt(1, "ebnf_or"),
                new Alt(2, "ebnf_repeat"),
                new Alt(3, "ebnf_repetition"),
                new Alt(4, "ebnf_group"),
                new Alt(5, "ebnf_terminal"),
                new Alt(6, "ebnf_non_terminal"),
                new Alt(7, "ebnf_inline_action"))
        ) {
            case 0 : {
                    ast.addNode("ebnf_optional").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_optional();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 1 : {
                    ast.addNode("ebnf_or").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_or();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 2 : {
                    ast.addNode("ebnf_repeat").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_repeat();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 3 : {
                    ast.addNode("ebnf_repetition").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_repetition();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 4 : {
                    ast.addNode("ebnf_group").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_group();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 5 : {
                    ast.addNode("ebnf_terminal").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_terminal();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 6 : {
                    ast.addNode("ebnf_non_terminal").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_non_terminal();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
            case 7 : {
                    ast.addNode("ebnf_inline_action").moveCursor(TO_LAST_NODE_CHILD);
                    ebnf_inline_action();
                    ast.moveCursor(TO_PARENT);
                    break;
                }
        }
    }

    private void ebnf_inline_action() {
        match("LEFT_INLINE_ACTION_BRACKET");
        match("STRING");
        match("RIGHT_INLINE_ACTION_BRACKET");
    }

    private void terminal_parameter_hidden() {
        match("TERMINAL_PARAMETER_HIDDEN");
        match("COLON");
        match("BOOLEAN", token -> {
            letters.peek().setHidden(token.getValue());
        });
    }

    private void terminal_description() {
        match("TERMINAL_TAG", token -> {
            letters.push(new LetterConstructor(lexerModule.tokenize(token.getValue())));
        });
        match("STRING", token -> {
            letters.peek().setPattern(stringProcessor.process(token.getValue()));
        });
        if(canReach("terminal_parameters")) {
            ast.addNode("terminal_parameters").moveCursor(TO_LAST_NODE_CHILD);
            terminal_parameters();
            ast.moveCursor(TO_PARENT);
        }
        letters.pop().construct();

    }


}