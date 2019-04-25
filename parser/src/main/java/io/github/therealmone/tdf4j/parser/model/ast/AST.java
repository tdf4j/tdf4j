/*
 * Copyright Roman Fatnev
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
package io.github.therealmone.tdf4j.parser.model.ast;

import io.github.therealmone.tdf4j.commons.Token;

import java.util.List;
import java.util.function.Consumer;

public class AST {
    private final ASTRoot root;
    private final Cursor cursor;

    public AST(final String rootTag) {
        this.root = ModifiableASTRoot.create()
                .setTag(rootTag);
        this.cursor = new Cursor(this.root);
    }

    public AST addNode(final String tag) {
        final ASTNode node = ModifiableASTNode.create()
                .setTag(tag)
                .setParent(cursor.getValue());
        addChild(cursor, node);
        return this;
    }

    public AST addLeaf(final Token token) {
        final ASTLeaf leaf = ModifiableASTLeaf.create()
                .setToken(token)
                .setParent(cursor.getValue());
        addChild(cursor, leaf);
        return this;
    }

    public AST moveCursor(final Consumer<Cursor> movement) {
        movement.accept(cursor);
        return this;
    }

    public AST moveCursor(final Movement movement) {
        movement.getMovement().accept(cursor);
        return this;
    }

    private void addChild(final Cursor cursor, final ASTElement element) {
        if(cursor.isNode()) {
            cursor.asNode().children().add(element);
        } else if(cursor.isRoot()) {
            cursor.asRoot().children().add(element);
        }
    }

    public ASTRoot getRoot() {
        return root;
    }

    public ASTElement onCursor() {
        return cursor.getValue();
    }

    public enum Movement {
        TO_PARENT(cursor -> {
            if(cursor.isNode()) {
                cursor.setValue(cursor.getValue().asNode().parent());
            } else if(cursor.getValue().isLeaf()) {
                cursor.setValue(cursor.getValue().asLeaf().parent());
            }
        }),

        TO_LAST_ADDED_NODE(cursor -> {
            if(cursor.isLeaf()) {
                return;
            }

            final List<ASTElement> children = cursor.isNode()
                    ? cursor.asNode().children()
                    : cursor.asRoot().children();
            for (int i = children.size() - 1; i >= 0; i--) {
                if(children.get(i).isNode()) {
                    cursor.setValue(children.get(i));
                    break;
                }
            }
        }),

        TO_LAST_ADDED_LEAF(cursor -> {
            if(cursor.isLeaf()) {
                return;
            }

            final List<ASTElement> children = cursor.isNode()
                    ? cursor.asNode().children()
                    : cursor.asRoot().children();
            for (int i = children.size() - 1; i >= 0; i--) {
                if(children.get(i).isLeaf()) {
                    cursor.setValue(children.get(i));
                    break;
                }
            }
        });

        private final Consumer<Cursor> movement;

        Movement(final Consumer<Cursor> movement) {
            this.movement = movement;
        }

        public Consumer<Cursor> getMovement() {
            return movement;
        }
    }

    public class Cursor implements ASTElement {
        private ASTElement value;

        Cursor(final ASTElement value) {
            this.value = value;
        }

        public void setValue(final ASTElement value) {
            this.value = value;
        }

        public ASTElement getValue() {
            return value;
        }

        @Override
        public Kind kind() {
            return value.kind();
        }

        @Override
        public ASTLeaf asLeaf() {
            return (ASTLeaf) value;
        }

        @Override
        public ASTNode asNode() {
            return (ASTNode) value;
        }

        @Override
        public ASTRoot asRoot() {
            return (ASTRoot) value;
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
