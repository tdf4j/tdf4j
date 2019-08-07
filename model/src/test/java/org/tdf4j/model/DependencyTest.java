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

package org.tdf4j.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DependencyTest {

    @Test
    public void full() {
        final TestDependency testDependency = new TestDependency();
        final Dependency<TestDependency> dependency = new Dependency.Builder<TestDependency>()
                .setClazz(TestDependency.class)
                .setName("test")
                .setInstance(testDependency)
                .build();
        assertEquals(TestDependency.class, dependency.getClazz());
        assertEquals("test", dependency.getName());
        assertEquals(testDependency, dependency.instance());
    }

    @Test
    public void without_instance() {
        final Dependency<TestDependency> dependency = new Dependency.Builder<TestDependency>()
                .setClazz(TestDependency.class)
                .setName("test")
                .build();
        assertEquals(TestDependency.class, dependency.getClazz());
        assertEquals("test", dependency.getName());
        assertEquals(TestDependency.class, dependency.instance().getClass());
    }

    @Test
    public void without_instance_and_default_constructor() {
        try {
            new Dependency.Builder<TestDependencyWithoutDefaultConstructor>()
                    .setClazz(TestDependencyWithoutDefaultConstructor.class)
                    .setName("test")
                    .build();
            fail("Excepted exception");
        } catch (RuntimeException e) {
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals("org.tdf4j.model.DependencyTest$TestDependencyWithoutDefaultConstructor.<init>()", e.getCause().getMessage());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class TestDependency {
        public TestDependency() {}
    }

    public static class TestDependencyWithoutDefaultConstructor {
        public TestDependencyWithoutDefaultConstructor(final int value) {}
    }
}
