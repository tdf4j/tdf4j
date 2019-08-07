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
package org.tdf4j.model.ast;

import java.util.List;
import java.util.function.Consumer;

public class ASTCursor implements ASTElement {

    private ASTElement value;

    ASTCursor(final ASTElement value) {
        this.value = value;
    }

    public void setValue(final ASTElement value) {
        this.value = value;
    }

    public ASTElement getValue() {
        return value;
    }

    @Override
    public ASTKind kind() {
        return value.kind();
    }

    @Override
    public <T extends ASTElement> T as(Class<T> clazz) {
        return clazz.cast(this.value);
    }

    public enum Movement implements Consumer<ASTCursor> {
        TO_PARENT(cursor -> {
            if(cursor.isNode()) {
                cursor.setValue(cursor.getValue().asNode().getParent());
            } else if(cursor.getValue().isLeaf()) {
                cursor.setValue(cursor.getValue().asLeaf().getParent());
            }
        }),

        TO_LAST_ADDED_NODE(cursor -> {
            if(cursor.isLeaf()) {
                return;
            }

            final List<ASTElement> children = cursor.isNode()
                    ? cursor.asNode().getChildren()
                    : cursor.asRoot().getChildren();
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
                    ? cursor.asNode().getChildren()
                    : cursor.asRoot().getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                if(children.get(i).isLeaf()) {
                    cursor.setValue(children.get(i));
                    break;
                }
            }
        });

        private final Consumer<ASTCursor> movement;

        Movement(final Consumer<ASTCursor> movement) {
            this.movement = movement;
        }

        @Override
        public void accept(final ASTCursor cursor) {
            this.movement.accept(cursor);
        }
    }
}
