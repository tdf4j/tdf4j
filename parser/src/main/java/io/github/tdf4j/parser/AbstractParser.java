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

package io.github.tdf4j.parser;

import io.github.tdf4j.core.model.Token;
import io.github.tdf4j.core.model.ast.AST;
import io.github.tdf4j.core.model.ast.ASTNode;
import io.github.tdf4j.core.model.ebnf.NonTerminal;
import io.github.tdf4j.core.model.ebnf.Terminal;
import io.github.tdf4j.core.utils.*;

import java.util.Arrays;
import java.util.function.Consumer;

import static io.github.tdf4j.core.model.ast.ASTCursor.Movement.*;
import static io.github.tdf4j.core.model.ebnf.EBNFBuilder.nonTerminal;

@SuppressWarnings("all")
public abstract class AbstractParser implements Parser {

    protected final Predictor predictor;

    protected BufferedStream<Token> stream;
    protected AST ast;

    public AbstractParser(final Predictor predictor) {
        this.predictor = predictor;
    }

    protected CallableNonTerminal callableNonTerminal(final String nonTerminal, final Callback callback) {
        return new CallableNonTerminal(nonTerminal(nonTerminal), callback);
    }

    protected boolean canReach(final Terminal terminal) {
        return canReach(terminal.getValue());
    }

    protected boolean canReach(final NonTerminal nonTerminal) {
        return canReach(nonTerminal.getValue());
    }

    protected boolean canReach(final String element) {
        return predictor.predict(stream.peek()).contains(element);
    }

    protected boolean canReachAny(final String... elements) {
        for(final String element : elements) {
            if(canReach(element)) {
                return true;
            }
        }
        return false;
    }

    protected void match(final Terminal terminal) {
        match(terminal, null);
    }

    protected void match(final Terminal terminal, final Consumer<Token> action) {
        if(stream.peek() != null && stream.peek().getTag().equals(terminal)) {
            ast.addLeaf(stream.next());
            if(action != null) {
                action.accept(ast.moveCursor(TO_LAST_LEAF_CHILD).onCursor().asLeaf().getToken());
                ast.moveCursor(TO_PARENT);
            }
        } else {
            throw new UnexpectedTokenException(stream.peek(), terminal.getValue());
        }
    }

    protected int predict(final Alt... alternatives) throws UnexpectedTokenException {
        final java.util.Optional<Alt> choice = Arrays.stream(alternatives)
                .filter(alt -> canReachAny(alt.elements))
                .findFirst();
        if(!choice.isPresent()) {
            throw new UnexpectedTokenException(stream.peek(), expected(alternatives));
        }
        return choice.get().index;
    }

    protected String[] expected(final Alt... alternatives) {
        return Arrays.stream(alternatives)
                .flatMap(alt -> Arrays.stream(alt.elements))
                .toArray(String[]::new);
    }

    protected void call(final CallableNonTerminal nonTerminal) {
        call(nonTerminal, null);
    }

    protected void call(final CallableNonTerminal nonTerminal, final Consumer<ASTNode> action) {
        ast.addNode(nonTerminal.getValue()).moveCursor(TO_LAST_NODE_CHILD);
        nonTerminal.call();
        if (action != null) {
            action.accept(ast.onCursor().asNode());
        }
        ast.moveCursor(TO_PARENT);
    }

}
