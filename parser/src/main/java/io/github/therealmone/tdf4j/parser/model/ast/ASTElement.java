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

public interface ASTElement {

    Kind kind();

    default boolean isLeaf() {
        return this.kind() == Kind.LEAF;
    }

    default ASTLeaf asLeaf() {
        return (ASTLeaf) this;
    }

    default boolean isNode() {
        return this.kind() == Kind.NODE;
    }

    default ASTNode asNode() {
        return (ASTNode) this;
    }

    default boolean isRoot() {
        return this.kind() == Kind.ROOT;
    }

    default ASTRoot asRoot() {
        return (ASTRoot) this;
    }

    enum Kind {
        NODE,
        LEAF,
        ROOT
    }
}
