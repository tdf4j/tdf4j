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

package org.tdf4j.utils;

import org.tdf4j.model.First;
import org.tdf4j.model.Production;
import org.junit.Ignore;
import org.junit.Test;
import org.tdf4j.model.ebnf.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FirstSetCollectorTest {
    private final FirstSetCollector firstSetCollector = new FirstSetCollector();

    @Test
    public void nested() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    nt("prod2"),
                    t("A"),
                    nt("prod3")
                ).build()
            );
            add(prod("prod2").is(
                    t("B")
            ).build());
            add(prod("prod3").is(
                    t("C")
            ).build());
        }});
        assertFirstContains(first, "prod1", "B");
        assertFirstContains(first, "prod2", "B");
        assertFirstContains(first, "prod3", "C");
    }

    @Test
    public void nested_with_group() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    group(nt("prod2")),
                    t("B"),
                    nt("prod3")
                    ).build()
            );
            add(prod("prod2").is(
                    group(t("B"))
            ).build());
            add(prod("prod3").is(
                    group(t("C"))
            ).build());
        }});
        assertFirstContains(first, "prod1", "B");
        assertFirstContains(first, "prod2", "B");
        assertFirstContains(first, "prod3", "C");
    }

    @Test
    public void nested_with_optional() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    optional(nt("prod2")),
                    t("B"),
                    nt("prod3")
                    ).build()
            );
            add(prod("prod2").is(
                    optional(t("B"))
            ).build());
            add(prod("prod3").is(
                    optional(t("C"))
            ).build());
        }});
        assertFirstContains(first, "prod1", "B");
        assertFirstContains(first, "prod2", "B");
        assertFirstContains(first, "prod3", "C");
    }

    @Test
    public void nested_with_or() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    or(
                            nt("prod2"),
                            t("A")
                    ),
                    nt("prod3")
                    ).build()
            );
            add(prod("prod2").is(
                    or(t("B"), t("C"))
            ).build());
            add(prod("prod3").is(
                    or(t("C"), nt("prod2"))
            ).build());
        }});
        assertFirstContains(first, "prod1", "B", "A", "C");
        assertFirstContains(first, "prod2", "B", "C");
        assertFirstContains(first, "prod3", "B", "C");
    }

    @Test
    public void nested_with_repeat() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    repeat(nt("prod2")),
                    t("B"),
                    nt("prod3")
                    ).build()
            );
            add(prod("prod2").is(
                    repeat(t("B"))
            ).build());
            add(prod("prod3").is(
                    repeat(t("C"))
            ).build());
        }});
        assertFirstContains(first, "prod1", "B");
        assertFirstContains(first, "prod2", "B");
        assertFirstContains(first, "prod3", "C");
    }

    @Test
    public void nested_with_repetition() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    repetition(nt("prod2"), 2),
                    t("B"),
                    nt("prod3")
                    ).build()
            );
            add(prod("prod2").is(
                    repetition(t("B"), 1)
            ).build());
            add(prod("prod3").is(
                    repetition(t("C"), 3)
            ).build());
        }});
        assertFirstContains(first, "prod1", "B");
        assertFirstContains(first, "prod2", "B");
        assertFirstContains(first, "prod3", "C");
    }

    @Test //todo
    @Ignore("todo")
    public void nested_with_except() {

    }

    @Test
    public void unknown_prod_indent() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    nt("prod2"),
                    t("B")
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void inline_action_as_first_element() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    inline("inline"),
                    t("A")
            ).build());
        }});
        assertFirstContains(first, "prod1", "A");
    }

    @Test
    public void inline_action_as_first_element_in_group() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    group(
                            inline("inline"),
                            t("A")
                    )
            ).build());
        }});
        assertFirstContains(first, "prod1", "A");
    }

    @Test
    public void empty_group() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    group()
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void recursion() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    nt("prod1")
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void empty_optional() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    optional()
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void empty_repeat() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    repeat()
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void repetition_element_null() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(new Production() {
                @Override
                public NonTerminal getIdentifier() {
                    return new NonTerminal.Builder().setValue("prod1").build();
                }

                @Override
                public List<Element> getElements() {
                    return new ArrayList<>() {{
                        add(new Repetition() {
                            @Override
                            public Element getElement() {
                                return null;
                            }

                            @Override
                            public int getTimes() {
                                return 100;
                            }
                        });
                    }};
                }
            });
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void unknown_element() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    (Element) () -> Element.Kind.TERMINAL
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void element_kind_null() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
                    (Element) () -> null
            ).build());
        }});
        assertFirstContains(first, "prod1");
    }

    @Test
    public void first_element_return_null() {
        assertNull(firstSetCollector.firstElement(new ArrayList<>() {{
            group(inline("code"));
        }}));
    }

    @Test
    public void context_get_production_return_null() {
        final FirstSetCollector.Context context = new FirstSetCollector.Context(new ArrayList<>() {{
            prod("prod1").is();
        }});
        assertNull(context.getProduction(nt("prod2")));
    }


    private Production.Builder prod(final String identifier) {
        return new Production.Builder().identifier(identifier);
    }

    private Optional optional(final Element... elements) {
        return new Optional.Builder().setElements(elements).build();
    }

    private Group group(final Element ... elements) {
        return new Group.Builder().setElements(elements).build();
    }

    private Repeat repeat(final Element ... elements) {
        return new Repeat.Builder().setElements(elements).build();
    }

    private Repetition repetition(final Element element, final int times) {
        return new Repetition.Builder().setElement(element).setTimes(times).build();
    }

    private Or or(final Element first, final Element second) {
        return new Or.Builder().setFirst(first).setSecond(second).build();
    }

    private Or oneOf(final Element... elements) {
        if(elements.length < 2) {
            throw new RuntimeException("oneOf() accepts 2 ore more elements");
        }

        if(elements.length == 2) {
            return new Or.Builder().setFirst(elements[0]).setSecond(elements[1]).build();
        } else {
            final Element[] toRecursion = new Element[elements.length - 1];
            System.arraycopy(elements, 1, toRecursion, 0, elements.length - 1);
            return new Or.Builder().setFirst(elements[0]).setSecond(oneOf(toRecursion)).build();
        }
    }

    private Terminal.Tag t(final String tag) {
        return new Terminal.Tag.Builder().setValue(tag).build();
    }

    private NonTerminal nt(final String identifier) {
        return new NonTerminal.Builder().setValue(identifier).build();
    }

    public InlineAction inline(final String code) {
        //noinspection ConstantConditions
        if(code == null || code.trim().equalsIgnoreCase("")) {
            throw new IllegalStateException("Code can't be blank or null");
        }
        return new InlineAction.Builder().setCode(code).build();
    }

    private static void assertFirstContains(final First first, final String ident, final String ... tags) {
        final Set<String> set = first.getSet()
                .get(new NonTerminal.Builder().setValue(ident).build())
                .stream()
                .map(Terminal.Tag::getValue)
                .collect(Collectors.toSet());
        assertEquals(tags.length, set.size());
        for(final String tag : tags) {
            assertTrue(set.contains(tag));
            set.remove(tag);
        }
        assertTrue(set.isEmpty());
    }
}
