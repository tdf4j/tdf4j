package io.github.therealmone.tdf4j.model.ast;

import io.github.therealmone.tdf4j.model.Token;
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
            assertEquals("LEAF1", child.asLeaf().getToken().getTag().getValue());
            assertEquals("leaf1", child.asLeaf().getToken().getValue());
            assertEquals(ast.onCursor(), child.asLeaf().getParent());
        }

        //fourth child
        {
            final ASTElement child = ast.onCursor().asRoot().getChildren().get(3);
            assertTrue(child.isLeaf());
            assertEquals("LEAF2", child.asLeaf().getToken().getTag().getValue());
            assertEquals("leaf2", child.asLeaf().getToken().getValue());
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
            assertEquals("LEAF1", ast.onCursor().asLeaf().getToken().getTag().getValue());
            assertEquals("leaf1", ast.onCursor().asLeaf().getToken().getValue());
            ast.moveCursor(ASTCursor.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().getTag());
        }

        //to second leaf
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().getChildren().get(1)));
            assertTrue(ast.onCursor().isLeaf());
            assertEquals("LEAF2", ast.onCursor().asLeaf().getToken().getTag().getValue());
            assertEquals("leaf2", ast.onCursor().asLeaf().getToken().getValue());
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
}
