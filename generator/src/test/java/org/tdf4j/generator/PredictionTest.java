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
import org.tdf4j.model.ebnf.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class PredictionTest {
    private final Prediction prediction = new Prediction();

    @Test
    public void get_start_element_from_null() {
        assertEquals(0, prediction.getStartElements(null).size());
    }

    @Test
    public void empty_repeat() {
        assertEquals(0, prediction.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void repeat() {
        assertEquals("A", prediction.getStartElements(new Repeat() {
            @Override
            public Element[] getElements() {
                return new Element[] {new Terminal.Tag.Builder().setValue("A").build()};
            }
        }).get(0));
    }

    @Test
    public void repeat_with_inline_action() {
        assertEquals("A", prediction.getStartElements(new Repeat() {
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
        assertEquals(0, prediction.getStartElements(new Repeat() {
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
        assertEquals(0, prediction.getStartElements(new Repetition() {
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
        assertEquals("A", prediction.getStartElements(new Repetition() {
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
        assertEquals(0, prediction.getStartElements(new Repetition() {
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
        assertEquals(0, prediction.getStartElements(new Or() {
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
        assertEquals(2, prediction.getStartElements(new Or() {
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
        assertEquals("A", prediction.getStartElements(new Or() {
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
        assertEquals(0, prediction.getStartElements(new Group() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void group() {
        assertEquals("A", prediction.getStartElements(new Group() {
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
        assertEquals("A", prediction.getStartElements(new Group() {
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
        assertEquals(0, prediction.getStartElements(new Group() {
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
        assertEquals(0, prediction.getStartElements(new Optional() {
            @Override
            public Element[] getElements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void optional() {
        assertEquals("A", prediction.getStartElements(new Optional() {
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
        assertEquals("A", prediction.getStartElements(new Optional() {
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
        assertEquals(0, prediction.getStartElements(new Optional() {
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
        assertEquals("A", prediction.getStartElements(new Terminal.Tag.Builder().setValue("A").build()).get(0));
    }

    @Test
    public void non_terminal() {
        assertEquals("prod1", prediction.getStartElements(new NonTerminal.Builder().setValue("prod1").build()).get(0));
    }

    @Test
    public void unknown_element() {
        assertEquals(0, prediction.getStartElements(new Terminal() {
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
        assertNull(prediction.firstNotInlineElement(new InlineAction.Builder().setCode("code").build()));
    }

    private class Prediction extends org.tdf4j.generator.templates.adaptor.Prediction {
        @Nonnull
        @Override
        protected List<String> getStartElements(@Nullable final Element element) {
            return super.getStartElements(element);
        }

        @Nullable
        @Override
        public Element firstNotInlineElement(final Element... elements) {
            return super.firstNotInlineElement(elements);
        }
    }
}
