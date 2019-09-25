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

package org.tdf4j.tdfparser.impl;

import org.tdf4j.parser.*;
import org.tdf4j.lexer.*;
import org.tdf4j.core.model.*;
import org.tdf4j.core.model.ast.*;
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
    private final Lexer lexer;


    private AST ast;
    private BufferedStream<Token> stream;

    public TdfParserImpl(
        final MetaInf meta,
        final Lexer lexer,
        final Predictor predictor
    ) {
        this.meta = meta;
        this.lexer = lexer;
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
    public AST parse(final CharSequence input) {
        this.stream = new BufferedStream<>(lexer.analyze(input));
        this.ast = AST.create("tdf_lang");
        tdf_lang();
        return ast;
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

    private void call(final String nonTerminal, final Callback callback) {
        call(nonTerminal, callback, null);
    }

    private void call(final String nonTerminal, final Callback callback, final Consumer<ASTNode> action) {
            ast.addNode(nonTerminal).moveCursor(TO_LAST_NODE_CHILD);
            callback.call();
            if (action != null) {
                action.accept(ast.onCursor().asNode());
            }
            ast.moveCursor(TO_PARENT);
        }

    @FunctionalInterface
    private interface Callback {
        void call();
    }

    private void ebnf_optional() {
        match("LEFT_SQUARE_BRACKET");
        call("ebnf_elements_set", this::ebnf_elements_set);
        match("RIGHT_SQUARE_BRACKET");
    }

    private void ebnf_elements_set() {
        call("ebnf_element", this::ebnf_element);
        if(canReach("COMMA")) {
            match("COMMA");
            call("ebnf_elements_set", this::ebnf_elements_set);
        }
    }

    private void ebnf_or() {
        for(int i487805674 = 0; i487805674 < 2; i487805674++) {
            match("LOP_OR");
            call("ebnf_element", this::ebnf_element);
        }
        while(true) {
            if(canReach("LOP_OR")) {
                match("LOP_OR");
                call("ebnf_element", this::ebnf_element);
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

        call("lexis", this::lexis);
        if(canReach("environment")) {
            call("environment", this::environment);
        }
        call("syntax", this::syntax);
        match("EOF");
    }

    private void lexis() {
        match("KEY_LEXIS");
        while(true) {
            if(canReach("terminal_description")) {
                call("terminal_description", this::terminal_description);
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
        call("pattern_flags", this::pattern_flags);
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
        call("ebnf_elements_set", this::ebnf_elements_set, node -> {
            productions.peek().setElements(node);
        });
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
            call("pattern_flags", this::pattern_flags);
        }
    }

    private void environment() {
        match("KEY_ENV");
        environments.push(new EnvironmentConstructor(parserModule.environment()));

        while(true) {
            if(canReach("env_import")) {
                call("env_import", this::env_import);
            } else {
                break;
            }
        }
        if(canReach("env_code")) {
            call("env_code", this::env_code);
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
                    call("terminal_parameter_priority", this::terminal_parameter_priority);
                    break;
                }
            case 1 : {
                    call("terminal_parameter_hidden", this::terminal_parameter_hidden);
                    break;
                }
            case 2 : {
                    call("terminal_parameter_pattern_flag", this::terminal_parameter_pattern_flag);
                    break;
                }
        }
        if(canReach("COMMA")) {
            match("COMMA");
            call("terminal_parameters_values", this::terminal_parameters_values);
        }
    }

    private void ebnf_repetition() {
        match("INTEGER");
        match("OP_MULTIPLY");
        call("ebnf_element", this::ebnf_element);
    }

    private void ebnf_group() {
        match("LEFT_BRACKET");
        call("ebnf_elements_set", this::ebnf_elements_set);
        match("RIGHT_BRACKET");
    }

    private void terminal_parameters() {
        match("LEFT_SQUARE_BRACKET");
        call("terminal_parameters_values", this::terminal_parameters_values);
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
        call("ebnf_elements_set", this::ebnf_elements_set);
        match("RIGHT_FIGURE_BRACKET");
    }

    private void syntax() {
        match("KEY_SYNTAX");
        while(true) {
            if(canReach("production_description")) {
                call("production_description", this::production_description);
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
                    call("ebnf_optional", this::ebnf_optional);
                    break;
                }
            case 1 : {
                    call("ebnf_or", this::ebnf_or);
                    break;
                }
            case 2 : {
                    call("ebnf_repeat", this::ebnf_repeat);
                    break;
                }
            case 3 : {
                    call("ebnf_repetition", this::ebnf_repetition);
                    break;
                }
            case 4 : {
                    call("ebnf_group", this::ebnf_group);
                    break;
                }
            case 5 : {
                    call("ebnf_terminal", this::ebnf_terminal);
                    break;
                }
            case 6 : {
                    call("ebnf_non_terminal", this::ebnf_non_terminal);
                    break;
                }
            case 7 : {
                    call("ebnf_inline_action", this::ebnf_inline_action);
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
            call("terminal_parameters", this::terminal_parameters);
        }
        letters.pop().construct();

    }


}