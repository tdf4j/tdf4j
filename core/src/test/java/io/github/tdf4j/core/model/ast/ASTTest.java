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

package io.github.tdf4j.core.model.ast;

import io.github.tdf4j.core.model.Token;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ASTTest {

    @Test
    public void add_child() {
        final AST ast = AST.create("lang")
                .addNode("node1")
                .addNode("node2")
                .addLeaf(new Token.Builder().setTag("leaf1").setValue("leaf1").build())
                .addLeaf(new Token.Builder().setTag("leaf2").setValue("leaf2").build());
        //check root
        {
            assertTrue(ast.onCursor().isRoot());
            assertEquals(4, ast.onCursor().asRoot().getChildren().size());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }

        //first child
        {
            final ASTElement child = ast.onCursor().asRoot().getChildren().get(0);
            assertTrue(child.isNode());
            assertEquals("node1", child.asNode().getTag());
            assertEquals(ast.onCursor(), child.asNode().getParent());
        }

        //second child
        {
            final ASTElement child = ast.onCursor().asRoot().getChildren().get(1);
            assertTrue(child.isNode());
            assertEquals("node2", child.asNode().getTag());
            assertEquals(ast.onCursor(), child.asNode().getParent());
        }

        //third child
        {
            final ASTElement child = ast.onCursor().asRoot().getChildren().get(2);
            assertTrue(child.isLeaf());
            Assert.assertEquals("LEAF1", child.asLeaf().getToken().getTag().getValue());
            Assert.assertEquals("leaf1", child.asLeaf().getToken().getValue());
            assertEquals(ast.onCursor(), child.asLeaf().getParent());
        }

        //fourth child
        {
            final ASTElement child = ast.onCursor().asRoot().getChildren().get(3);
            assertTrue(child.isLeaf());
            Assert.assertEquals("LEAF2", child.asLeaf().getToken().getTag().getValue());
            Assert.assertEquals("leaf2", child.asLeaf().getToken().getValue());
            assertEquals(ast.onCursor(), child.asLeaf().getParent());
        }
    }

    @Test
    public void move_to_parent_from_node() {
        final AST ast = AST.create("lang")
                .addNode("node1")
                .addNode("node2");

        //to first node
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().getChildren().get(0)));
            assertTrue(ast.onCursor().isNode());
            assertEquals("node1", ast.onCursor().asNode().getTag());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }

        //to second node
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().getChildren().get(1)));
            assertTrue(ast.onCursor().isNode());
            assertEquals("node2", ast.onCursor().asNode().getTag());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }
    }

    @Test
    public void move_to_parent_from_leaf() {
        final AST ast = AST.create("lang")
                .addLeaf(new Token.Builder().setTag("leaf1").setValue("leaf1").build())
                .addLeaf(new Token.Builder().setTag("leaf2").setValue("leaf2").build());

        //to first leaf
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().getChildren().get(0)));
            assertTrue(ast.onCursor().isLeaf());
            Assert.assertEquals("LEAF1", ast.onCursor().asLeaf().getToken().getTag().getValue());
            Assert.assertEquals("leaf1", ast.onCursor().asLeaf().getToken().getValue());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }

        //to second leaf
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().getChildren().get(1)));
            assertTrue(ast.onCursor().isLeaf());
            Assert.assertEquals("LEAF2", ast.onCursor().asLeaf().getToken().getTag().getValue());
            Assert.assertEquals("leaf2", ast.onCursor().asLeaf().getToken().getValue());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }
    }

    @Test
    public void move_to_parent_from_root() {
        final AST ast = AST.create("lang")
                .addLeaf(new Token.Builder().setTag("leaf1").setValue("leaf1").build())
                .addLeaf(new Token.Builder().setTag("leaf2").setValue("leaf2").build());

        ast.moveCursor(cursor -> cursor.setValue(cursor.getValue())); //xd
        assertTrue(ast.onCursor().isRoot());
        assertEquals("lang", ast.onCursor().asRoot().getTag());
        ast.moveCursor(ASTCursor.Movement.TO_PARENT);
        assertTrue(ast.onCursor().isRoot());
        assertEquals("lang", ast.onCursor().asRoot().getTag());
    }

    @Test
    public void last_leaf() {
        final AST ast = AST.create("lang");
        assertNull(ast.getLastLeaf());
        ast.addLeaf(new Token.Builder().setTag("leaf1").setValue("leaf1").build());
        ast.addLeaf(new Token.Builder().setTag("leaf2").setValue("leaf2").build());
        assertEquals("leaf2", ast.getLastLeaf().getToken().getValue());
        assertEquals("lang", ast.onCursor().asRoot().getTag());
    }

    @Test
    public void last_node() {
        final AST ast = AST.create("lang");
        assertNull(ast.getLastNode());
        ast.addNode("node1");
        ast.addNode("node2");
        assertEquals("node2", ast.getLastNode().getTag());
        assertEquals("lang", ast.onCursor().asRoot().getTag());
    }
}
