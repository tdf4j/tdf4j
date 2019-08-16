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
    private final Stack<TerminalConstructor> terminals = new Stack<>();
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

    private String lastValue(final AST ast) {
       final String value = ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken().getValue();
       ast.moveCursor(ASTCursor.Movement.TO_PARENT);
       return value;
    }

    private ASTNode lastNode(final AST ast) {
       final ASTNode lastNode = ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE).onCursor().asNode();
       ast.moveCursor(ASTCursor.Movement.TO_PARENT);
       return lastNode;
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
                action.accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
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

    @SuppressWarnings("ConstantConditions")
    private void ebnf_optional() {
        match("LEFT_SQUARE_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("RIGHT_SQUARE_BRACKET");
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_elements_set() {
        ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_element();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(canReach("COMMA")) {
            match("COMMA");
            ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            ebnf_elements_set();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_or() {
        for(int i487805674 = 0; i487805674 < 2; i487805674++) {
            match("LOP_OR");
            ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            ebnf_element();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        while(true) {
            if(canReach("LOP_OR")) {
                match("LOP_OR");
                ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                ebnf_element();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void tdf_lang() {
        this.lexerModule = new LexerAbstractModule() {
           @Override
           public void configure() {}
        };

        this.parserModule = new ParserAbstractModule() {
           @Override
           public void configure() {}
        };

        ast.addNode("lexis").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        lexis();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(canReach("environment")) {
            ast.addNode("environment").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            environment();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        ast.addNode("syntax").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        syntax();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("EOF");
    }

    @SuppressWarnings("ConstantConditions")
    private void lexis() {
        match("KEY_LEXIS");
        while(true) {
            if(canReach("terminal_description")) {
                ast.addNode("terminal_description").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                terminal_description();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void env_import() {
        match("KEY_IMPORT");
        match("STRING", token -> {
            environments.peek().addPackage(stringProcessor.process(token.getValue()));
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_pattern_flag() {
        match("TERMINAL_PARAMETER_PATTERN_FLAG");
        match("COLON");
        ast.addNode("pattern_flags").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        pattern_flags();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_priority() {
        match("TERMINAL_PARAMETER_PRIORITY");
        match("COLON");
        match("INTEGER", token -> {
            terminals.peek().setPriority(token.getValue());
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void production_description() {
        match("NON_TERMINAL", token -> {
            productions.push(new ProductionConstructor(parserModule.prod(token.getValue())));
        });
        match("OP_ASSIGN");
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ((Consumer<ASTNode>) node -> {
            productions.peek().setElements(node);
        }).accept(ast.onCursor().asNode());
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("DELIMITER");
        productions.pop().construct();

    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_terminal() {
        match("TERMINAL_TAG");
        if(canReach("LAMBDA")) {
            match("LAMBDA");
            match("STRING");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void pattern_flags() {
        switch(predict(new Alt(0, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES"), new Alt(1, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE"), new Alt(2, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS"), new Alt(3, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE"), new Alt(4, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL"), new Alt(5, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL"), new Alt(6, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE"), new Alt(7, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ"), new Alt(8, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS"))) {
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
        terminals.peek().addFlag(lastValue(ast));

        if(canReach("OP_SUM")) {
            match("OP_SUM");
            ast.addNode("pattern_flags").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            pattern_flags();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void environment() {
        match("KEY_ENV");
        environments.push(new EnvironmentConstructor(parserModule.environment()));

        while(true) {
            if(canReach("env_import")) {
                ast.addNode("env_import").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                env_import();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                break;
            }
        }
        if(canReach("env_code")) {
            ast.addNode("env_code").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            env_code();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        environments.pop().construct();

    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_non_terminal() {
        match("NON_TERMINAL");
        if(canReach("LAMBDA")) {
            match("LAMBDA");
            match("STRING");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameters_values() {
        switch(predict(new Alt(0, "terminal_parameter_priority"), new Alt(1, "terminal_parameter_hidden"), new Alt(2, "terminal_parameter_pattern_flag"))) {
            case 0 : {
                    ast.addNode("terminal_parameter_priority").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    terminal_parameter_priority();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 1 : {
                    ast.addNode("terminal_parameter_hidden").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    terminal_parameter_hidden();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 2 : {
                    ast.addNode("terminal_parameter_pattern_flag").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    terminal_parameter_pattern_flag();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
        }
        if(canReach("COMMA")) {
            match("COMMA");
            ast.addNode("terminal_parameters_values").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            terminal_parameters_values();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_repetition() {
        match("INTEGER");
        match("OP_MULTIPLY");
        ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_element();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_group() {
        match("LEFT_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("RIGHT_BRACKET");
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameters() {
        match("LEFT_SQUARE_BRACKET");
        ast.addNode("terminal_parameters_values").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        terminal_parameters_values();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("RIGHT_SQUARE_BRACKET");
    }

    @SuppressWarnings("ConstantConditions")
    private void env_code() {
        match("KEY_CODE");
        match("STRING", token -> {
            environments.peek().setCode(stringProcessor.process(token.getValue()));

        });
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_repeat() {
        match("LEFT_FIGURE_BRACKET");
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        match("RIGHT_FIGURE_BRACKET");
    }

    @SuppressWarnings("ConstantConditions")
    private void syntax() {
        match("KEY_SYNTAX");
        while(true) {
            if(canReach("production_description")) {
                ast.addNode("production_description").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                production_description();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_element() {
        switch(predict(new Alt(0, "ebnf_optional"), new Alt(1, "ebnf_or"), new Alt(2, "ebnf_repeat"), new Alt(3, "ebnf_repetition"), new Alt(4, "ebnf_group"), new Alt(5, "ebnf_terminal"), new Alt(6, "ebnf_non_terminal"), new Alt(7, "ebnf_inline_action"))) {
            case 0 : {
                    ast.addNode("ebnf_optional").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_optional();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 1 : {
                    ast.addNode("ebnf_or").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_or();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 2 : {
                    ast.addNode("ebnf_repeat").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_repeat();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 3 : {
                    ast.addNode("ebnf_repetition").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_repetition();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 4 : {
                    ast.addNode("ebnf_group").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_group();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 5 : {
                    ast.addNode("ebnf_terminal").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_terminal();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 6 : {
                    ast.addNode("ebnf_non_terminal").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_non_terminal();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
            case 7 : {
                    ast.addNode("ebnf_inline_action").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_inline_action();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    break;
                }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_inline_action() {
        match("LEFT_INLINE_ACTION_BRACKET");
        match("STRING");
        match("RIGHT_INLINE_ACTION_BRACKET");
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_hidden() {
        match("TERMINAL_PARAMETER_HIDDEN");
        match("COLON");
        match("BOOLEAN", token -> {
            terminals.peek().setHidden(token.getValue());
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_description() {
        match("TERMINAL_TAG", token -> {
            terminals.push(new TerminalConstructor(lexerModule.tokenize(token.getValue())));
        });
        match("STRING", token -> {
            terminals.peek().setPattern(stringProcessor.process(token.getValue()));
        });
        if(canReach("terminal_parameters")) {
            ast.addNode("terminal_parameters").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            terminal_parameters();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        terminals.pop().construct();

    }


}