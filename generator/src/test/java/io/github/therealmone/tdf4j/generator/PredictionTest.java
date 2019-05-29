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
            public Element[] elements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void repeat() {
        assertEquals("A", prediction.getStartElements(new Repeat() {
            @Override
            public Element[] elements() {
                return new Element[] {new Terminal.Tag.Builder().value("A").build()};
            }
        }).get(0));
    }

    @Test
    public void repeat_with_inline_action() {
        assertEquals("A", prediction.getStartElements(new Repeat() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build(),
                        new Terminal.Tag.Builder().value("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void repeat_inline_action_only() {
        assertEquals(0, prediction.getStartElements(new Repeat() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build()
                };
            }
        }).size());
    }

    @Test
    public void empty_repetition() {
        assertEquals(0, prediction.getStartElements(new Repetition() {
            @Override
            public Element element() {
                return null;
            }

            @Override
            public int times() {
                return 0;
            }
        }).size());
    }

    @Test
    public void repetition() {
        assertEquals("A", prediction.getStartElements(new Repetition() {
            @Override
            public Element element() {
                return new Terminal.Tag.Builder().value("A").build();
            }

            @Override
            public int times() {
                return 0;
            }
        }).get(0));
    }

    @Test
    public void repetition_with_inline_action() {
        assertEquals(0, prediction.getStartElements(new Repetition() {
            @Override
            public Element element() {
                return new InlineAction.Builder().code("code").build();
            }

            @Override
            public int times() {
                return 0;
            }
        }).size());
    }

    @Test
    public void or_with_nulls() {
        assertEquals(0, prediction.getStartElements(new Or() {
            @Override
            public Element first() {
                return null;
            }

            @Override
            public Element second() {
                return null;
            }
        }).size());
    }

    @Test
    public void or() {
        assertEquals(2, prediction.getStartElements(new Or() {
            @Override
            public Element first() {
                return new Terminal.Tag.Builder().value("A").build();
            }

            @Override
            public Element second() {
                return new Terminal.Tag.Builder().value("B").build();
            }
        }).size());
    }

    @Test
    public void or_with_inline_actions() {
        assertEquals("A", prediction.getStartElements(new Or() {
            @Override
            public Element first() {
                return new InlineAction.Builder().code("code").build();
            }

            @Override
            public Element second() {
                return new Terminal.Tag.Builder().value("A").build();
            }
        }).get(0));
    }

    @Test
    public void empty_group() {
        assertEquals(0, prediction.getStartElements(new Group() {
            @Override
            public Element[] elements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void group() {
        assertEquals("A", prediction.getStartElements(new Group() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new Terminal.Tag.Builder().value("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action() {
        assertEquals("A", prediction.getStartElements(new Group() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build(),
                        new Terminal.Tag.Builder().value("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void group_with_inline_action_only() {
        assertEquals(0, prediction.getStartElements(new Group() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build()
                };
            }
        }).size());
    }

    @Test
    public void empty_optional() {
        assertEquals(0, prediction.getStartElements(new Optional() {
            @Override
            public Element[] elements() {
                return new Element[0];
            }
        }).size());
    }

    @Test
    public void optional() {
        assertEquals("A", prediction.getStartElements(new Optional() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new Terminal.Tag.Builder().value("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action() {
        assertEquals("A", prediction.getStartElements(new Optional() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build(),
                        new Terminal.Tag.Builder().value("A").build()
                };
            }
        }).get(0));
    }

    @Test
    public void optional_with_inline_action_only() {
        assertEquals(0, prediction.getStartElements(new Optional() {
            @Override
            public Element[] elements() {
                return new Element[] {
                        new InlineAction.Builder().code("code").build()
                };
            }
        }).size());
    }

    @Test
    public void tag() {
        assertEquals("A", prediction.getStartElements(new Terminal.Tag.Builder().value("A").build()).get(0));
    }

    @Test
    public void non_terminal() {
        assertEquals("prod1", prediction.getStartElements(new NonTerminal.Builder().identifier("prod1").build()).get(0));
    }

    @Test
    public void unknown_element() {
        assertEquals(0, prediction.getStartElements(new Terminal() {
            @Override
            public Tag tag() {
                return null;
            }

            @Override
            public Pattern pattern() {
                return null;
            }
        }).size());
    }

    @Test
    public void first_not_inline_element() {
        assertNull(prediction.firstNotInlineElement(new InlineAction.Builder().code("code").build()));
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
