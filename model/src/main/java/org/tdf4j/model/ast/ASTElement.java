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

public interface ASTElement {

    ASTKind kind();

    default boolean is(final ASTKind kind) {
        return this.kind() == kind;
    }

    default <T extends ASTElement> T as(final Class<T> clazz) {
        return clazz.cast(this);
    }

    default ASTNode asNode() {
        return as(ASTNode.class);
    }

    default ASTLeaf asLeaf() {
        return as(ASTLeaf.class);
    }

    default ASTRoot asRoot() {
        return as(ASTRoot.class);
    }

    default boolean isNode() {
        return is(ASTKind.NODE);
    }

    default boolean isLeaf() {
        return is(ASTKind.LEAF);
    }

    default boolean isRoot() {
        return is(ASTKind.ROOT);
    }
}
