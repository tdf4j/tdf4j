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
import org.tdf4j.core.utils.Elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class PredictionTest {

    @Test
    public void get_start_element_from_null() {
        assertEquals(0, Elements.getStartElements(null).size());
    }

    @Test
    public void empty_repeat() {
        assertEquals(0, Elements.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void repeat() {
        assertEquals("A", Elements.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {new Terminal.Tag.Builder().setValue("A").build()};
            }
        }).get(0));
    }

    @Test
    public void repeat_with_inline_action() {
        assertEquals("A", Elements.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build(),
                        new Terminal.Tag.Builder().setValue("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void repeat_inline_action_only() {
        assertEquals(0, Elements.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build()
                };
            }
        }).size());
    }

    @Test
    public void empty_repetition() {
        assertEquals(0, Elements.getStartElements(new Repetition() {
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
        assertEquals("A", Elements.getStartElements(new Repetition() {
            @Override
            public Element getElement() {
                return new Terminal.Tag.Builder().setValue("A").build();
            }

            @Override
            public int getTimes() {
                return 0;
            }
        }).get(0));
    }

    @Test
    public void repetition_with_inline_action() {
        assertEquals(0, Elements.getStartElements(new Repetition() {
            @Override
            public Element getElement() {
                return new InlineAction.Builder().setCode("code").build();
            }

            @Override
            public int getTimes() {
                return 0;
            }
        }).size());
    }

    @Test
    public void or_with_nulls() {
        assertEquals(0, Elements.getStartElements(new Or() {
            @Override
            public Element getFirst() {
                return null;
            }

            @Override
            public Element getSecond() {
                return null;
            }
        }).size());
    }

    @Test
    public void or() {
        assertEquals(2, Elements.getStartElements(new Or() {
            @Override
            public Element getFirst() {
                return new Terminal.Tag.Builder().setValue("A").build();
            }

            @Override
            public Element getSecond() {
                return new Terminal.Tag.Builder().setValue("B").build();
            }
        }).size());
    }

    @Test
    public void or_with_inline_actions() {
        assertEquals("A", Elements.getStartElements(new Or() {
            @Override
            public Element getFirst() {
                return new InlineAction.Builder().setCode("code").build();
            }

            @Override
            public Element getSecond() {
                return new Terminal.Tag.Builder().setValue("A").build();
            }
        }).get(0));
    }

    @Test
    public void empty_group() {
        assertEquals(0, Elements.getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void group() {
        assertEquals("A", Elements.getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new Terminal.Tag.Builder().setValue("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action() {
        assertEquals("A", Elements.getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build(),
                        new Terminal.Tag.Builder().setValue("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action_only() {
        assertEquals(0, Elements.getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build()
                };
            }
        }).size());
    }

    @Test
    public void empty_optional() {
        assertEquals(0, Elements.getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void optional() {
        assertEquals("A", Elements.getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new Terminal.Tag.Builder().setValue("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action() {
        assertEquals("A", Elements.getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build(),
                        new Terminal.Tag.Builder().setValue("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action_only() {
        assertEquals(0, Elements.getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[] {
                        new InlineAction.Builder().setCode("code").build()
                };
            }
        }).size());
    }

    @Test
    public void tag() {
        assertEquals("A", Elements.getStartElements(new Terminal.Tag.Builder().setValue("A").build()).get(0));
    }

    @Test
    public void non_terminal() {
        assertEquals("prod1", Elements.getStartElements(new NonTerminal.Builder().setValue("prod1").build()).get(0));
    }

    @Test
    public void unknown_element() {
        assertEquals(0, Elements.getStartElements(new Terminal() {
            @Override
            public Tag getTag() {
                return null;
            }

            @Override
            public Pattern getPattern() {
                return null;
            }
        }).size());
    }

    @Test
    public void first_not_inline_element() {
        assertNull(Elements.firstNotInlineElement(new InlineAction.Builder().setCode("code").build()));
    }
}
