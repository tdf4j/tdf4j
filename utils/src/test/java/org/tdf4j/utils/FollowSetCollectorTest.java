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

import org.tdf4j.model.Follow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FollowSetCollectorTest {
    private final FollowSetCollector followSetCollector = new FollowSetCollector();

    //todo
    @Test
    public void test() {
        final Follow follow = followSetCollector.collect(new ArrayList<>());
        assertNotNull(follow);
        assertEquals(0, follow.getSet().size());
    }

}
