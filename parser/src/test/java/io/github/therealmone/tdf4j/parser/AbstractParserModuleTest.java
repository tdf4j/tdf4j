package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractParserModuleTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(t("ele1"))
                        .then(t("ele2"));
                prod("prod2")
                        .is(nt("ele1"), nt("ele2"));
            }
        }.build();
        assertEquals(2, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        {
            assertEquals("prod2", module.getProductions().get(0).identifier());
            assertEquals(2, module.getProductions().get(0).elements().size());
            assertEquals("ele1", module.getProductions().get(0).elements().get(0).asNonTerminal().identifier());
            assertEquals("ele2", module.getProductions().get(0).elements().get(1).asNonTerminal().identifier());
        }
        {
            assertEquals("prod1", module.getProductions().get(1).identifier());
            assertEquals(2, module.getProductions().get(1).elements().size());
            assertEquals("ele1", module.getProductions().get(1).elements().get(0).asTerminalTag().value());
            assertEquals("ele2", module.getProductions().get(1).elements().get(1).asTerminalTag().value());
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
                        .then(optional(t("name1"), t("name2"), t("name3")))
                        .then(optional())
                        .then(optional(or(nt("name4"), nt("name5"))))
                        .then(optional(optional(), t("name6")));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        final Production production = module.getProductions().get(0);
        assertEquals("prod1", production.identifier());

        //.then(optional(name("name1"), name("name2"), name("name3")))
        {
            assertTrue(production.elements().get(0).isOptional());
            assertEquals(3, production.elements().get(0).asOptional().elements().length);
            assertTrue(production.elements().get(0).asOptional().elements()[0].isTerminalTag());
            assertTrue(production.elements().get(0).asOptional().elements()[1].isTerminalTag());
            assertTrue(production.elements().get(0).asOptional().elements()[2].isTerminalTag());
            assertEquals("name1", production.elements().get(0).asOptional().elements()[0].asTerminalTag().value());
            assertEquals("name2", production.elements().get(0).asOptional().elements()[1].asTerminalTag().value());
            assertEquals("name3", production.elements().get(0).asOptional().elements()[2].asTerminalTag().value());
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
            assertTrue(production.elements().get(2).asOptional().elements()[0].asOr().first().isNonTerminal());
            assertTrue(production.elements().get(2).asOptional().elements()[0].asOr().second().isNonTerminal());
            assertEquals("name4", production.elements().get(2).asOptional().elements()[0].asOr().first().asNonTerminal().identifier());
            assertEquals("name5", production.elements().get(2).asOptional().elements()[0].asOr().second().asNonTerminal().identifier());
        }

        //.then(optional(optional(), name("name6")));
        {
            assertTrue(production.elements().get(3).isOptional());
            assertEquals(2, production.elements().get(3).asOptional().elements().length);
            assertTrue(production.elements().get(3).asOptional().elements()[0].isOptional());
            assertEquals(0, production.elements().get(3).asOptional().elements()[0].asOptional().elements().length);
            assertTrue(production.elements().get(3).asOptional().elements()[1].isTerminalTag());
            assertEquals("name6", production.elements().get(3).asOptional().elements()[1].asTerminalTag().value());
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
                        .then(repeat(t("name1"), t("name2"), t("name3")))
                        .then(repeat())
                        .then(repeat(
                                or(
                                        t("name4"),
                                        or(t("name5"), t("name6"))
                                )
                            )
                        )
                        .then(repeat(repeat(), optional()));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        assertEquals("prod1 := {name1,name2,name3},{},{name4|name5|name6},{{},[]}", module.getProductions().get(0).toString());
    }

    @Test
    public void repetition_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repetition(t("A"), 5))
                        .then(repetition(repetition(t("A"), 0), 1_000_000))
                        .then(repetition(group(t("A"), t("B")), 2))
                        .then(repetition(optional(t("A"), t("B")), 3))
                        .then(repetition(repeat(t("A"), t("B")), 4));
            }
        }.build();
        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        assertEquals("prod1 := 5*A,1000000*0*A,2*(A,B),3*[A,B],4*{A,B}", module.getProductions().get(0).toString());
    }

    @Test
    public void group_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(group(t("name1"), t("name2")), t("name3")))
                        .then(group())
                        .then(group(optional(t("name4")), t("name5"), t("name6")))
                        .then(group(or(optional(), repeat()), group()));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        assertEquals("prod1 := (name1,name2)|name3,(),([name4],name5,name6),([]|{},())", module.getProductions().get(0).toString());
    }

    @Test
    public void or_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                t("name1"),
                                or(
                                        t("name2"),
                                        or(
                                                t("name3"),
                                                group(t("name4"), t("name5"))
                                        )
                                )
                        ));
            }
        }.build();

        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        assertEquals("prod1 := name1|name2|name3|(name4,name5)", module.getProductions().get(0).toString());
    }

    @Test
    public void init_prod_test() {
        //if no prods set - null
        {
            final AbstractParserModule module = new AbstractParserModule() {
                @Override
                public void configure() {
                }
            }.build();
            assertNull(module.getInitProduction());
        }

        //if init prod was not specified - first prod
        {
            final AbstractParserModule module = new AbstractParserModule() {
                @Override
                public void configure() {
                    prod("prod1");
                    prod("prod2");
                    prod("prod3");
                }
            }.build();
            assertEquals("prod1", module.getInitProduction());
        }

        //specified
        {
            final AbstractParserModule module = new AbstractParserModule() {
                @Override
                public void configure() {
                    prod("prod1");
                    prod("prod2");
                    prod("prod3");
                    initProd("prod3");
                }
            }.build();
            assertEquals("prod3", module.getInitProduction());
        }
    }

    @Test
    public void except_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(nt("prod2"))
                        .then(except(t("C")));
            }
        }.build();
        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getProductions().get(0).identifier());
        assertEquals("prod1 := prod2,-(C)", module.getProductions().get(0).toString());
    }

    @Test
    public void one_of_test() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(oneOf(t("A"), t("B"), t("C")))
                        .then(oneOf(optional(t("C")), t("A")));
            }
        }.build();
        assertEquals(1, module.getProductions().size());
        assertEquals("prod1", module.getInitProduction());
        assertEquals("prod1 := A|B|C,[C]|A", module.getProductions().get(0).toString());
    }
}
