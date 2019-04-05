package io.github.therealmone.tdf4j.commons;

import io.github.therealmone.tdf4j.commons.model.ebnf.*;
import io.github.therealmone.tdf4j.commons.utils.FirstSetCollector;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FirstSetCollectorTest {
    private final FirstSetCollector firstSetCollector = new FirstSetCollector();

    @Test
    public void nested() {
        final First first = firstSetCollector.collect(new ArrayList<>() {{
            add(prod("prod1").is(
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

    private Production.Builder prod(final String identifier) {
        return new Production.Builder().identifier(identifier);
    }

    private Optional optional(final Element... elements) {
        return new Optional.Builder().elements(elements).build();
    }

    private Group group(final Element ... elements) {
        return new Group.Builder().elements(elements).build();
    }

    private Repeat repeat(final Element ... elements) {
        return new Repeat.Builder().elements(elements).build();
    }

    private Repetition repetition(final Element element, final int times) {
        return new Repetition.Builder().element(element).times(times).build();
    }

    private Or or(final Element first, final Element second) {
        return new Or.Builder().first(first).second(second).build();
    }

    private Or oneOf(Element... elements) {
        if(elements.length < 2) {
            throw new RuntimeException("oneOf() accepts 2 ore more elements");
        }

        if(elements.length == 2) {
            return new Or.Builder().first(elements[0]).second(elements[1]).build();
        } else {
            final Element[] toRecursion = new Element[elements.length - 1];
            System.arraycopy(elements, 1, toRecursion, 0, elements.length - 1);
            return new Or.Builder().first(elements[0]).second(oneOf(toRecursion)).build();
        }
    }

    private Terminal.Tag t(final String tag) {
        return new Terminal.Tag.Builder().value(tag).build();
    }

    private NonTerminal nt(final String identifier) {
        return new NonTerminal.Builder().identifier(identifier).build();
    }

    private Except except(final Terminal.Tag ... tags) {
        return new Except.Builder().tags(tags).build();
    }

    private static void assertFirstContains(final First first, final String ident, final String ... tags) {
        final Set<String> set = first.set().get(ident).stream().map(Terminal.Tag::value).collect(Collectors.toSet());
        for(final String tag : tags) {
            assertTrue(set.contains(tag));
            set.remove(tag);
        }
        assertTrue(set.isEmpty());
    }
}
