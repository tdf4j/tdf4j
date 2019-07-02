package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.model.ebnf.*;
import org.junit.Test;

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
        assertEquals("prod1", prediction.getStartElements(new NonTerminal.Builder().setIdentifier("prod1").build()).get(0));
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

    private class Prediction extends io.github.therealmone.tdf4j.generator.templates.logic.Prediction {
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
