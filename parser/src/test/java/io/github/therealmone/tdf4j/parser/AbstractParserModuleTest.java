package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.model.ebnf.Production;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractParserModuleTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(name("ele1"))
                        .then(name("ele2"));
                prod("prod2")
                        .then(name("ele1"), name("ele2"));
            }
        }.build();
        assertEquals(2, module.getProductions().size());
        {
            assertEquals("prod2", module.getProductions().get(0).identifier());
            assertEquals(2, module.getProductions().get(0).elements().size());
            assertEquals("ele1", module.getProductions().get(0).elements().get(0).asName().value());
            assertEquals("ele2", module.getProductions().get(0).elements().get(1).asName().value());
        }
        {
            assertEquals("prod1", module.getProductions().get(1).identifier());
            assertEquals(2, module.getProductions().get(1).elements().size());
            assertEquals("ele1", module.getProductions().get(1).elements().get(0).asName().value());
            assertEquals("ele2", module.getProductions().get(1).elements().get(1).asName().value());
        }
    }

    @Test
    public void no_prods() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
            }
        }.build();
        assertEquals(0, module.getProductions().size());
    }

    @Test(expected = RuntimeException.class)
    public void null_ident() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod(null);
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void blank_ident() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("   ");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void unique_identifiers() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1");
                prod("prod1");
            }
        }.build();
    }

    @Test
    public void optional_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(optional(name("name1"), name("name2"), name("name3")))
                        .then(optional())
                        .then(optional(or(name("name4"), name("name5"))))
                        .then(optional(optional(), name("name6")));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        final Production production = module.getProductions().get(0);
        assertEquals("prod1", production.identifier());

        //.then(optional(name("name1"), name("name2"), name("name3")))
        {
            assertTrue(production.elements().get(0).isOptional());
            assertEquals(3, production.elements().get(0).asOptional().elements().length);
            assertTrue(production.elements().get(0).asOptional().elements()[0].isName());
            assertTrue(production.elements().get(0).asOptional().elements()[1].isName());
            assertTrue(production.elements().get(0).asOptional().elements()[2].isName());
            assertEquals("name1", production.elements().get(0).asOptional().elements()[0].asName().value());
            assertEquals("name2", production.elements().get(0).asOptional().elements()[1].asName().value());
            assertEquals("name3", production.elements().get(0).asOptional().elements()[2].asName().value());
        }

        //.then(optional())
        {
            assertTrue(production.elements().get(1).isOptional());
            assertEquals(0, production.elements().get(1).asOptional().elements().length);
        }

        //.then(optional(or(name("name4"), name("name5"))))
        {
            assertTrue(production.elements().get(2).isOptional());
            assertEquals(1, production.elements().get(2).asOptional().elements().length);
            assertTrue(production.elements().get(2).asOptional().elements()[0].isOr());
            assertTrue(production.elements().get(2).asOptional().elements()[0].asOr().first().isName());
            assertTrue(production.elements().get(2).asOptional().elements()[0].asOr().second().isName());
            assertEquals("name4", production.elements().get(2).asOptional().elements()[0].asOr().first().asName().value());
            assertEquals("name5", production.elements().get(2).asOptional().elements()[0].asOr().second().asName().value());
        }

        //.then(optional(optional(), name("name6")));
        {
            assertTrue(production.elements().get(3).isOptional());
            assertEquals(2, production.elements().get(3).asOptional().elements().length);
            assertTrue(production.elements().get(3).asOptional().elements()[0].isOptional());
            assertEquals(0, production.elements().get(3).asOptional().elements()[0].asOptional().elements().length);
            assertTrue(production.elements().get(3).asOptional().elements()[1].isName());
            assertEquals("name6", production.elements().get(3).asOptional().elements()[1].asName().value());
        }

        //toString()
        {
            assertEquals("prod1 := [name1,name2,name3],[],[name4|name5],[[],name6]", production.toString());
        }
    }

    @Test
    public void repeat_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(name("name1"), name("name2"), name("name3")))
                        .then(repeat())
                        .then(repeat(
                                or(
                                        name("name4"),
                                        or(name("name5"), name("name6"))
                                )
                            )
                        )
                        .then(repeat(repeat(), optional()));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1 := {name1,name2,name3},{},{name4|name5|name6},{{},[]}", module.getProductions().get(0).toString());
    }

    @Test
    public void group_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(group(name("name1"), name("name2")), name("name3")))
                        .then(group())
                        .then(group(optional(name("name4")), name("name5"), name("name6")))
                        .then(group(or(optional(), repeat()), group()));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1 := (name1,name2)|name3,(),([name4],name5,name6),([]|{},())", module.getProductions().get(0).toString());
    }

    @Test
    public void or_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                name("name1"),
                                or(
                                        name("name2"),
                                        or(
                                                name("name3"),
                                                group(name("name4"), name("name5"))
                                        )
                                )
                        ));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1 := name1|name2|name3|(name4,name5)", module.getProductions().get(0).toString());
    }
}
