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

package org.tdf4j.generator;

import org.junit.Test;
import org.tdf4j.core.model.ebnf.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.tdf4j.core.model.ebnf.EBNFBuilder.*;
import static org.tdf4j.core.model.ebnf.Elements.*;

public class PredictionTest {

    @Test
    public void get_start_element_from_null() {
        assertEquals(0, getStartElements(null).size());
    }

    @Test
    public void empty_repeat() {
        assertEquals(0, getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void repeat() {
        assertEquals("A", getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {terminal("A")};
            }
        }).get(0));
    }

    @Test
    public void repeat_with_inline_action() {
        assertEquals("A", getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code"),
                        terminal("A")
                };
            }
        }).get(0));
    }

    @Test
    public void repeat_inline_action_only() {
        assertEquals(0, getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code")
                };
            }
        }).size());
    }

    @Test
    public void empty_repetition() {
        assertEquals(0, getStartElements(new Repetition() {
            @Override
            public Element getElement() {
                return null;
            }

            @Override
            public int getTimes() {
                return 0;
            }
        }).size());
    }

    @Test
    public void repetition() {
        assertEquals("A", getStartElements(new Repetition() {
            @Override
            public Element getElement() {
                return terminal("A");
            }

            @Override
            public int getTimes() {
                return 0;
            }
        }).get(0));
    }

    @Test
    public void repetition_with_inline_action() {
        assertEquals(0, getStartElements(new Repetition() {
            @Override
            public Element getElement() {
                return inlineAction("code");
            }

            @Override
            public int getTimes() {
                return 0;
            }
        }).size());
    }

    @Test
    public void or_with_nulls() {
        assertEquals(0, getStartElements(new Or() {
            @Override
            public List<Alternative> getAlternatives() {
                return null;
            }
        }).size());
    }

    @Test
    public void or() {
        assertEquals(2, getStartElements(new Or() {
            @Override
            public List<Alternative> getAlternatives() {
                return new ArrayList<>() {{
                    add(alternative(0, terminal("A")));
                    add(alternative(0, terminal("B")));
                }};
            }
        }).size());
    }

    @Test
    public void or_with_inline_actions() {
        assertEquals("A", getStartElements(new Or() {
            @Override
            public List<Alternative> getAlternatives() {
                return new ArrayList<>() {{
                    add(alternative(0, inlineAction("code")));
                    add(alternative(0, terminal("A")));
                }};
            }
        }).get(0));
    }

    @Test
    public void empty_group() {
        assertEquals(0, getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void group() {
        assertEquals("A", getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        terminal("A")
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action() {
        assertEquals("A", getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code"),
                        terminal("A")
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action_only() {
        assertEquals(0, getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code")
                };
            }
        }).size());
    }

    @Test
    public void empty_optional() {
        assertEquals(0, getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void optional() {
        assertEquals("A", getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        terminal("A")
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action() {
        assertEquals("A", getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code"),
                        terminal("A")
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action_only() {
        assertEquals(0, getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        inlineAction("code")
                };
            }
        }).size());
    }

    @Test
    public void tag() {
        assertEquals("A", getStartElements(terminal("A")).get(0));
    }

    @Test
    public void non_terminal() {
        assertEquals("prod1", getStartElements(nonTerminal("prod1")).get(0));
    }

    @Test
    public void unknown_element() {
        assertEquals(0, getStartElements(new InlineAction() {
            @Override
            public String getCode() {
                return null;
            }
        }).size());
    }

    @Test
    public void first_not_inline_element() {
        assertNull(firstNotInlineElement(inlineAction("code")));
    }
}
