/*
 * Copyright 2019 Roman Fatnev
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
package io.github.therealmone.tdf4j.model.ast;

import io.github.therealmone.tdf4j.model.Token;

import javax.annotation.Nullable;
import java.util.function.Consumer;

class ASTImpl implements AST {
    private final ASTRoot root;
    private final ASTCursor cursor;

    ASTImpl(final ASTRoot root) {
        this.root = root;
        this.cursor = new ASTCursor(this.root);
    }

    @Override
    public AST addNode(final String tag) {
        return addNode(ModifiableASTNode.create()
                .setTag(tag)
                .setParent(cursor.getValue())
        );
    }

    @Override
    public AST addNode(final ASTNode node) {
        addChild(cursor, node);
        return this;
    }

    @Override
    @Nullable
    public ASTNode lastNode() {
        final ASTElement element = this.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE).onCursor();
        return element != null && element.isNode()
                ? element.asNode()
                : null;
    }

    @Override
    public AST addLeaf(final Token token) {
        return addLeaf(ModifiableASTLeaf.create()
                .setToken(token)
                .setParent(cursor.getValue())
        );
    }

    @Override
    public AST addLeaf(final ASTLeaf leaf) {
        addChild(cursor, leaf);
        return this;
    }

    @Override
    @Nullable
    public ASTLeaf lastLeaf() {
        final ASTElement element = this.moveCursor(ASTCursor.Movement.TO_LAST_ADDED_NODE).onCursor();
        return element != null && element.isLeaf()
                ? element.asLeaf()
                : null;
    }

    @Override
    public ASTImpl moveCursor(final Consumer<ASTCursor> movement) {
        movement.accept(cursor);
        return this;
    }

    @Override
    public ASTImpl moveCursor(final ASTCursor.Movement movement) {
        movement.accept(cursor);
        return this;
    }

    private void addChild(final ASTCursor cursor, final ASTElement element) {
        if(cursor.isNode()) {
            cursor.asNode().getChildren().add(element);
        } else if(cursor.isRoot()) {
            cursor.asRoot().getChildren().add(element);
        }
    }

    @Override
    public ASTRoot getRoot() {
        return root;
    }

    @Override
    public ASTElement onCursor() {
        return cursor.getValue();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
