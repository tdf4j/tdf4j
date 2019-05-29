package io.github.therealmone.tdf4j.model.ast;

import io.github.therealmone.tdf4j.model.ebnf.AbstractElement;
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Group;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractElementTest {

    @Test
    public void to_string_group() {
        final AET aet = new AET();
        assertEquals("(A,B,C)", aet.toStringGroup(
                new Group() {
                    @Override
                    public Element[] elements() {
                        return new Element[] {
                                new Terminal.Tag.Builder().value("A").build(),
                                new Terminal.Tag.Builder().value("B").build(),
                                new Terminal.Tag.Builder().value("C").build()
                        };
                    }
                }
        ));
    }

    @Test
    public void to_string_empty_group() {
        final AET aet = new AET();
        assertEquals("", aet.toStringGroup());
    }

    class AET extends AbstractElement {
        @Override
        public Kind kind() {
            return null;
        }

        @Override
        public String toStringGroup(final Element... elements) {
            return super.toStringGroup(elements);
        }
    }
}
