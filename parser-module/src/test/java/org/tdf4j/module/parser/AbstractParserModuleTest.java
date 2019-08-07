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

package org.tdf4j.module.parser;

import org.tdf4j.model.Grammar;
import org.tdf4j.model.Production;
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
        final Grammar grammar = module.getGrammar();
        assertEquals(2, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        {
            assertEquals("prod2", grammar.getProductions().get(0).getIdentifier().getValue());
            assertEquals(2, grammar.getProductions().get(0).getElements().size());
            assertEquals("ele1", grammar.getProductions().get(0).getElements().get(0).asNonTerminal().getValue());
            assertEquals("ele2", grammar.getProductions().get(0).getElements().get(1).asNonTerminal().getValue());
        }
        {
            assertEquals("prod1", grammar.getProductions().get(1).getIdentifier().getValue());
            assertEquals(2, grammar.getProductions().get(1).getElements().size());
            assertEquals("ELE1", grammar.getProductions().get(1).getElements().get(0).asTerminalTag().getValue());
            assertEquals("ELE2", grammar.getProductions().get(1).getElements().get(1).asTerminalTag().getValue());
        }
    }

    @Test
    public void no_prods() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
            }
        }.build();
        assertEquals(0, module.getGrammar().getProductions().size());
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

        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        final Production production = grammar.getProductions().get(0);
        assertEquals("prod1", production.getIdentifier().getValue());

        //.then(optional(name("name1"), name("name2"), name("name3")))
        {
            assertTrue(production.getElements().get(0).isOptional());
            assertEquals(3, production.getElements().get(0).asOptional().getElements().length);
            assertTrue(production.getElements().get(0).asOptional().getElements()[0].isTerminalTag());
            assertTrue(production.getElements().get(0).asOptional().getElements()[1].isTerminalTag());
            assertTrue(production.getElements().get(0).asOptional().getElements()[2].isTerminalTag());
            assertEquals("NAME1", production.getElements().get(0).asOptional().getElements()[0].asTerminalTag().getValue());
            assertEquals("NAME2", production.getElements().get(0).asOptional().getElements()[1].asTerminalTag().getValue());
            assertEquals("NAME3", production.getElements().get(0).asOptional().getElements()[2].asTerminalTag().getValue());
        }

        //.then(optional())
        {
            assertTrue(production.getElements().get(1).isOptional());
            assertEquals(0, production.getElements().get(1).asOptional().getElements().length);
        }

        //.then(optional(or(name("name4"), name("name5"))))
        {
            assertTrue(production.getElements().get(2).isOptional());
            assertEquals(1, production.getElements().get(2).asOptional().getElements().length);
            assertTrue(production.getElements().get(2).asOptional().getElements()[0].isOr());
            assertTrue(production.getElements().get(2).asOptional().getElements()[0].asOr().getFirst().isNonTerminal());
            assertTrue(production.getElements().get(2).asOptional().getElements()[0].asOr().getSecond().isNonTerminal());
            assertEquals("name4", production.getElements().get(2).asOptional().getElements()[0].asOr().getFirst().asNonTerminal().getValue());
            assertEquals("name5", production.getElements().get(2).asOptional().getElements()[0].asOr().getSecond().asNonTerminal().getValue());
        }

        //.then(optional(optional(), name("name6")));
        {
            assertTrue(production.getElements().get(3).isOptional());
            assertEquals(2, production.getElements().get(3).asOptional().getElements().length);
            assertTrue(production.getElements().get(3).asOptional().getElements()[0].isOptional());
            assertEquals(0, production.getElements().get(3).asOptional().getElements()[0].asOptional().getElements().length);
            assertTrue(production.getElements().get(3).asOptional().getElements()[1].isTerminalTag());
            assertEquals("NAME6", production.getElements().get(3).asOptional().getElements()[1].asTerminalTag().getValue());
        }

        //toString()
        {
            assertEquals("prod1 := [NAME1,NAME2,NAME3],[],[name4|name5],[[],NAME6]", production.toString());
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

        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        assertEquals("prod1 := {NAME1,NAME2,NAME3},{},{NAME4|NAME5|NAME6},{{},[]}", grammar.getProductions().get(0).toString());
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
        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        assertEquals("prod1 := 5*A,1000000*0*A,2*(A,B),3*[A,B],4*{A,B}", grammar.getProductions().get(0).toString());
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
        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        assertEquals("prod1 := (NAME1,NAME2)|NAME3,(),([NAME4],NAME5,NAME6),([]|{},())", grammar.getProductions().get(0).toString());
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
        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        assertEquals("prod1 := NAME1|NAME2|NAME3|(NAME4,NAME5)", grammar.getProductions().get(0).toString());
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
            assertNull(module.getGrammar().getAxiom());
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
            assertEquals("prod1", module.getGrammar().getAxiom());
        }

        //specified
        {
            final AbstractParserModule module = new AbstractParserModule() {
                @Override
                public void configure() {
                    prod("prod1");
                    prod("prod2");
                    prod("prod3");
                    axiom("prod3");
                }
            }.build();
            assertEquals("prod3", module.getGrammar().getAxiom());
        }
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
        final Grammar grammar = module.getGrammar();
        assertEquals(1, grammar.getProductions().size());
        assertEquals("prod1", grammar.getAxiom());
        assertEquals("prod1 := A|B|C,[C]|A", grammar.getProductions().get(0).toString());
    }
}
