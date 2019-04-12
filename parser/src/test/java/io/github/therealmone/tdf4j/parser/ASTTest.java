package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.parser.model.ast.AST;
import io.github.therealmone.tdf4j.parser.model.ast.ASTElement;
import org.junit.Test;

import static org.junit.Assert.*;

public class ASTTest {

    @Test
    public void add_child() {
        final AST ast = new AST("lang")
                .addNode("node1")
                .addNode("node2")
                .addLeaf(new Token.Builder().tag("leaf1").value("leaf1").build())
                .addLeaf(new Token.Builder().tag("leaf2").value("leaf2").build());
        //check root
        {
            assertTrue(ast.onCursor().isRoot());
            assertEquals(4, ast.onCursor().asRoot().children().size());
            assertEquals("lang", ast.onCursor().asRoot().tag());
        }

        //first child
        {
            final ASTElement child = ast.onCursor().asRoot().children().get(0);
            assertTrue(child.isNode());
            assertEquals("node1", child.asNode().tag());
            assertEquals(ast.onCursor(), child.asNode().parent());
        }

        //second child
        {
            final ASTElement child = ast.onCursor().asRoot().children().get(1);
            assertTrue(child.isNode());
            assertEquals("node2", child.asNode().tag());
            assertEquals(ast.onCursor(), child.asNode().parent());
        }

        //third child
        {
            final ASTElement child = ast.onCursor().asRoot().children().get(2);
            assertTrue(child.isLeaf());
            assertEquals("leaf1", child.asLeaf().token().tag());
            assertEquals("leaf1", child.asLeaf().token().value());
            assertEquals(ast.onCursor(), child.asLeaf().parent());
        }

        //fourth child
        {
            final ASTElement child = ast.onCursor().asRoot().children().get(3);
            assertTrue(child.isLeaf());
            assertEquals("leaf2", child.asLeaf().token().tag());
            assertEquals("leaf2", child.asLeaf().token().value());
            assertEquals(ast.onCursor(), child.asLeaf().parent());
        }
    }

    @Test
    public void move_to_parent_from_node() {
        final AST ast = new AST("lang")
                .addNode("node1")
                .addNode("node2");

        //to first node
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().children().get(0)));
            assertTrue(ast.onCursor().isNode());
            assertEquals("node1", ast.onCursor().asNode().tag());
            ast.moveCursor(AST.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().tag());
        }

        //to second node
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().children().get(1)));
            assertTrue(ast.onCursor().isNode());
            assertEquals("node2", ast.onCursor().asNode().tag());
            ast.moveCursor(AST.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().tag());
        }
    }

    @Test
    public void move_to_parent_from_leaf() {
        final AST ast = new AST("lang")
                .addLeaf(new Token.Builder().tag("leaf1").value("leaf1").build())
                .addLeaf(new Token.Builder().tag("leaf2").value("leaf2").build());

        //to first leaf
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().children().get(0)));
            assertTrue(ast.onCursor().isLeaf());
            assertEquals("leaf1", ast.onCursor().asLeaf().token().tag());
            assertEquals("leaf1", ast.onCursor().asLeaf().token().value());
            ast.moveCursor(AST.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().tag());
        }

        //to second leaf
        {
            ast.moveCursor(cursor -> cursor.setValue(cursor.asRoot().children().get(1)));
            assertTrue(ast.onCursor().isLeaf());
            assertEquals("leaf2", ast.onCursor().asLeaf().token().tag());
            assertEquals("leaf2", ast.onCursor().asLeaf().token().value());
            ast.moveCursor(AST.Movement.TO_PARENT);
            assertTrue(ast.onCursor().isRoot());
            assertEquals("lang", ast.onCursor().asRoot().tag());
        }
    }

    @Test
    public void move_to_parent_from_root() {
        final AST ast = new AST("lang")
                .addLeaf(new Token.Builder().tag("leaf1").value("leaf1").build())
                .addLeaf(new Token.Builder().tag("leaf2").value("leaf2").build());

        ast.moveCursor(cursor -> cursor.setValue(cursor.getValue())); //xd
        assertTrue(ast.onCursor().isRoot());
        assertEquals("lang", ast.onCursor().asRoot().tag());
        ast.moveCursor(AST.Movement.TO_PARENT);
        assertTrue(ast.onCursor().isRoot());
        assertEquals("lang", ast.onCursor().asRoot().tag());
    }
}
