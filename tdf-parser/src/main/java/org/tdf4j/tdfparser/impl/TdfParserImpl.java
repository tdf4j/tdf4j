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

package org.tdf4j.tdfparser.impl;

import org.tdf4j.parser.*;
import org.tdf4j.model.*;
import org.tdf4j.utils.*;
import java.util.*;
import java.util.function.*;
import org.tdf4j.tdfparser.TdfParser;
import org.tdf4j.model.Stream;
import org.tdf4j.model.Token;
import org.tdf4j.model.ast.AST;
import org.tdf4j.model.ast.ASTCursor;
import org.tdf4j.model.ast.ASTNode;
import org.tdf4j.module.lexer.AbstractLexerModule;
import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.tdfparser.constructor.*;
import org.tdf4j.tdfparser.processor.*;
import org.tdf4j.parser.MetaInf;
import org.tdf4j.parser.UnexpectedTokenException;
import org.tdf4j.tdfparser.constructor.EnvironmentConstructor;
import org.tdf4j.tdfparser.constructor.ProductionConstructor;
import org.tdf4j.tdfparser.constructor.TerminalConstructor;
import org.tdf4j.tdfparser.processor.Processor;
import org.tdf4j.tdfparser.processor.StringProcessor;
import org.tdf4j.utils.BufferedStream;
import org.tdf4j.utils.Predictor;

class TdfParserImpl implements TdfParser {
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
    private AbstractLexerModule lexerModule;
    private AbstractParserModule parserModule;

    @Override
    public AbstractLexerModule getLexerModule() {
       return this.lexerModule;
    }

    @Override
    public AbstractParserModule getParserModule() {
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

    @SuppressWarnings("ConstantConditions")
    private void ebnf_optional() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LEFT_SQUARE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LEFT_SQUARE_BRACKET");
        }
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("RIGHT_SQUARE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "RIGHT_SQUARE_BRACKET");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_elements_set() {
        ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_element();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(predictor.predict(stream.peek()).contains("COMMA")) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("COMMA")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "COMMA");
            }
            ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            ebnf_elements_set();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_or() {
        for(int i487805674 = 0; i487805674 < 2; i487805674++) {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LOP_OR")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LOP_OR");
        }
        ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_element();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        while(true) {
            if(predictor.predict(stream.peek()).contains("LOP_OR")) {
                if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LOP_OR")) {
                    ast.addLeaf(stream.next());
                } else {
                    throw new UnexpectedTokenException(stream.peek(), "LOP_OR");
                }
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
        this.lexerModule = new AbstractLexerModule() {
           @Override
           public void configure() {}
        };

        this.parserModule = new AbstractParserModule() {
           @Override
           public void configure() {}
        };

        ast.addNode("lexis").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        lexis();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(predictor.predict(stream.peek()).contains("environment")) {
            ast.addNode("environment").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            environment();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        ast.addNode("syntax").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        syntax();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("EOF")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "EOF");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void lexis() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("KEY_LEXIS")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "KEY_LEXIS");
        }
        while(true) {
            if(predictor.predict(stream.peek()).contains("terminal_description")) {
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
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("KEY_IMPORT")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "KEY_IMPORT");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                environments.peek().addPackage(stringProcessor.process(token.getValue()));
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "STRING");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_pattern_flag() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("COLON")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "COLON");
        }
        ast.addNode("pattern_flags").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        pattern_flags();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_priority() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PRIORITY")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PRIORITY");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("COLON")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "COLON");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("INTEGER")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                terminals.peek().setPriority(token.getValue());
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "INTEGER");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void production_description() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("NON_TERMINAL")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                productions.push(new ProductionConstructor(parserModule.prod(token.getValue())));
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "NON_TERMINAL");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("OP_ASSIGN")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "OP_ASSIGN");
        }
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ((Consumer<ASTNode>) node -> {
            productions.peek().setElements(node);
        }).accept(ast.onCursor().asNode());
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("DELIMITER")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "DELIMITER");
        }
        productions.pop().construct();

    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_terminal() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_TAG")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_TAG");
        }
        if(predictor.predict(stream.peek()).contains("LAMBDA")) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LAMBDA")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "LAMBDA");
            }
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "STRING");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void pattern_flags() {
        if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES")
        )) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES");
            }
        } else if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
        )) {
            if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE")
            )) {
                if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE")) {
                    ast.addLeaf(stream.next());
                } else {
                    throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE");
                }
            } else if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
            )) {
                if(stream.peek() != null && (
                    predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS")
                )) {
                    if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS")) {
                        ast.addLeaf(stream.next());
                    } else {
                        throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS");
                    }
                } else if(stream.peek() != null && (
                    predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE")
                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")
                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                )) {
                    if(stream.peek() != null && (
                        predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE")
                    )) {
                        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE")) {
                            ast.addLeaf(stream.next());
                        } else {
                            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE");
                        }
                    } else if(stream.peek() != null && (
                        predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")
                        || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
                        || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                        || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                        || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                    )) {
                        if(stream.peek() != null && (
                            predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")
                        )) {
                            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL")) {
                                ast.addLeaf(stream.next());
                            } else {
                                throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL");
                            }
                        } else if(stream.peek() != null && (
                            predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
                            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                            || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                        )) {
                            if(stream.peek() != null && (
                                predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")
                            )) {
                                if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL")) {
                                    ast.addLeaf(stream.next());
                                } else {
                                    throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL");
                                }
                            } else if(stream.peek() != null && (
                                predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                                || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                            )) {
                                if(stream.peek() != null && (
                                    predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")
                                )) {
                                    if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE")) {
                                        ast.addLeaf(stream.next());
                                    } else {
                                        throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE");
                                    }
                                } else if(stream.peek() != null && (
                                    predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                                    || predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                                )) {
                                    if(stream.peek() != null && (
                                        predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                                    )) {
                                        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")) {
                                            ast.addLeaf(stream.next());
                                        } else {
                                            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ");
                                        }
                                    } else if(stream.peek() != null && (
                                        predictor.predict(stream.peek()).contains("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")
                                    )) {
                                        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS")) {
                                            ast.addLeaf(stream.next());
                                        } else {
                                            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                                        }
                                    } else {
                                        throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                                    }
                                } else {
                                    throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                                }
                            } else {
                                throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                            }
                        } else {
                            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                        }
                    } else {
                        throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                    }
                } else {
                    throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
                }
            } else {
                throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
            }
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ" , "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
        }
        terminals.peek().addFlag(lastValue(ast));

        if(predictor.predict(stream.peek()).contains("OP_SUM")) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("OP_SUM")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "OP_SUM");
            }
            ast.addNode("pattern_flags").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            pattern_flags();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void environment() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("KEY_ENV")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "KEY_ENV");
        }
        environments.push(new EnvironmentConstructor(parserModule.environment()));

        while(true) {
            if(predictor.predict(stream.peek()).contains("env_import")) {
                ast.addNode("env_import").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                env_import();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                break;
            }
        }
        if(predictor.predict(stream.peek()).contains("env_code")) {
            ast.addNode("env_code").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            env_code();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        environments.pop().construct();

    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_non_terminal() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("NON_TERMINAL")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "NON_TERMINAL");
        }
        if(predictor.predict(stream.peek()).contains("LAMBDA")) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LAMBDA")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "LAMBDA");
            }
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "STRING");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameters_values() {
        if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("terminal_parameter_priority")
        )) {
            ast.addNode("terminal_parameter_priority").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            terminal_parameter_priority();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("terminal_parameter_hidden")
            || predictor.predict(stream.peek()).contains("terminal_parameter_pattern_flag")
        )) {
            if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("terminal_parameter_hidden")
            )) {
                ast.addNode("terminal_parameter_hidden").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                terminal_parameter_hidden();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("terminal_parameter_pattern_flag")
            )) {
                ast.addNode("terminal_parameter_pattern_flag").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                terminal_parameter_pattern_flag();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else {
                throw new UnexpectedTokenException(stream.peek(), "terminal_parameter_hidden" , "terminal_parameter_pattern_flag");
            }
        } else {
            throw new UnexpectedTokenException(stream.peek(), "terminal_parameter_priority" , "terminal_parameter_hidden" , "terminal_parameter_pattern_flag");
        }
        if(predictor.predict(stream.peek()).contains("COMMA")) {
            if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("COMMA")) {
                ast.addLeaf(stream.next());
            } else {
                throw new UnexpectedTokenException(stream.peek(), "COMMA");
            }
            ast.addNode("terminal_parameters_values").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            terminal_parameters_values();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_repetition() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("INTEGER")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "INTEGER");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("OP_MULTIPLY")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "OP_MULTIPLY");
        }
        ast.addNode("ebnf_element").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_element();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_group() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LEFT_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LEFT_BRACKET");
        }
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("RIGHT_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "RIGHT_BRACKET");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameters() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LEFT_SQUARE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LEFT_SQUARE_BRACKET");
        }
        ast.addNode("terminal_parameters_values").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        terminal_parameters_values();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("RIGHT_SQUARE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "RIGHT_SQUARE_BRACKET");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void env_code() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("KEY_CODE")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "KEY_CODE");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                environments.peek().setCode(stringProcessor.process(token.getValue()));

            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "STRING");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_repeat() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LEFT_FIGURE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LEFT_FIGURE_BRACKET");
        }
        ast.addNode("ebnf_elements_set").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
        ebnf_elements_set();
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("RIGHT_FIGURE_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "RIGHT_FIGURE_BRACKET");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void syntax() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("KEY_SYNTAX")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "KEY_SYNTAX");
        }
        while(true) {
            if(predictor.predict(stream.peek()).contains("production_description")) {
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
        if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("ebnf_optional")
        )) {
            ast.addNode("ebnf_optional").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            ebnf_optional();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else if(stream.peek() != null && (
            predictor.predict(stream.peek()).contains("ebnf_or")
            || predictor.predict(stream.peek()).contains("ebnf_repeat")
            || predictor.predict(stream.peek()).contains("ebnf_repetition")
            || predictor.predict(stream.peek()).contains("ebnf_group")
            || predictor.predict(stream.peek()).contains("ebnf_terminal")
            || predictor.predict(stream.peek()).contains("ebnf_non_terminal")
            || predictor.predict(stream.peek()).contains("ebnf_inline_action")
        )) {
            if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("ebnf_or")
            )) {
                ast.addNode("ebnf_or").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                ebnf_or();
                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            } else if(stream.peek() != null && (
                predictor.predict(stream.peek()).contains("ebnf_repeat")
                || predictor.predict(stream.peek()).contains("ebnf_repetition")
                || predictor.predict(stream.peek()).contains("ebnf_group")
                || predictor.predict(stream.peek()).contains("ebnf_terminal")
                || predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                || predictor.predict(stream.peek()).contains("ebnf_inline_action")
            )) {
                if(stream.peek() != null && (
                    predictor.predict(stream.peek()).contains("ebnf_repeat")
                )) {
                    ast.addNode("ebnf_repeat").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                    ebnf_repeat();
                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                } else if(stream.peek() != null && (
                    predictor.predict(stream.peek()).contains("ebnf_repetition")
                    || predictor.predict(stream.peek()).contains("ebnf_group")
                    || predictor.predict(stream.peek()).contains("ebnf_terminal")
                    || predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                    || predictor.predict(stream.peek()).contains("ebnf_inline_action")
                )) {
                    if(stream.peek() != null && (
                        predictor.predict(stream.peek()).contains("ebnf_repetition")
                    )) {
                        ast.addNode("ebnf_repetition").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                        ebnf_repetition();
                        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                    } else if(stream.peek() != null && (
                        predictor.predict(stream.peek()).contains("ebnf_group")
                        || predictor.predict(stream.peek()).contains("ebnf_terminal")
                        || predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                        || predictor.predict(stream.peek()).contains("ebnf_inline_action")
                    )) {
                        if(stream.peek() != null && (
                            predictor.predict(stream.peek()).contains("ebnf_group")
                        )) {
                            ast.addNode("ebnf_group").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                            ebnf_group();
                            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                        } else if(stream.peek() != null && (
                            predictor.predict(stream.peek()).contains("ebnf_terminal")
                            || predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                            || predictor.predict(stream.peek()).contains("ebnf_inline_action")
                        )) {
                            if(stream.peek() != null && (
                                predictor.predict(stream.peek()).contains("ebnf_terminal")
                            )) {
                                ast.addNode("ebnf_terminal").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                                ebnf_terminal();
                                ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                            } else if(stream.peek() != null && (
                                predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                                || predictor.predict(stream.peek()).contains("ebnf_inline_action")
                            )) {
                                if(stream.peek() != null && (
                                    predictor.predict(stream.peek()).contains("ebnf_non_terminal")
                                )) {
                                    ast.addNode("ebnf_non_terminal").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                                    ebnf_non_terminal();
                                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                                } else if(stream.peek() != null && (
                                    predictor.predict(stream.peek()).contains("ebnf_inline_action")
                                )) {
                                    ast.addNode("ebnf_inline_action").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
                                    ebnf_inline_action();
                                    ast.moveCursor(ASTCursor.Movement.TO_PARENT);
                                } else {
                                    throw new UnexpectedTokenException(stream.peek(), "ebnf_non_terminal" , "ebnf_inline_action");
                                }
                            } else {
                                throw new UnexpectedTokenException(stream.peek(), "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
                            }
                        } else {
                            throw new UnexpectedTokenException(stream.peek(), "ebnf_group" , "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
                        }
                    } else {
                        throw new UnexpectedTokenException(stream.peek(), "ebnf_repetition" , "ebnf_group" , "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
                    }
                } else {
                    throw new UnexpectedTokenException(stream.peek(), "ebnf_repeat" , "ebnf_repetition" , "ebnf_group" , "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
                }
            } else {
                throw new UnexpectedTokenException(stream.peek(), "ebnf_or" , "ebnf_repeat" , "ebnf_repetition" , "ebnf_group" , "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
            }
        } else {
            throw new UnexpectedTokenException(stream.peek(), "ebnf_optional" , "ebnf_or" , "ebnf_repeat" , "ebnf_repetition" , "ebnf_group" , "ebnf_terminal" , "ebnf_non_terminal" , "ebnf_inline_action");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void ebnf_inline_action() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("LEFT_INLINE_ACTION_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "LEFT_INLINE_ACTION_BRACKET");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "STRING");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("RIGHT_INLINE_ACTION_BRACKET")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "RIGHT_INLINE_ACTION_BRACKET");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_parameter_hidden() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_PARAMETER_HIDDEN")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_PARAMETER_HIDDEN");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("COLON")) {
            ast.addLeaf(stream.next());
        } else {
            throw new UnexpectedTokenException(stream.peek(), "COLON");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("BOOLEAN")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                terminals.peek().setHidden(token.getValue());
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "BOOLEAN");
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void terminal_description() {
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("TERMINAL_TAG")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                terminals.push(new TerminalConstructor(lexerModule.tokenize(token.getValue())));
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "TERMINAL_TAG");
        }
        if(stream.peek() != null && stream.peek().getTag().getValue().equalsIgnoreCase("STRING")) {
            ast.addLeaf(stream.next());
            ((Consumer<Token>) token -> {
                terminals.peek().setPattern(stringProcessor.process(token.getValue()));
            }).accept(ast.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_LEAF).onCursor().asLeaf().getToken());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        } else {
            throw new UnexpectedTokenException(stream.peek(), "STRING");
        }
        if(predictor.predict(stream.peek()).contains("terminal_parameters")) {
            ast.addNode("terminal_parameters").moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE);
            terminal_parameters();
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        }
        terminals.pop().construct();

    }


}