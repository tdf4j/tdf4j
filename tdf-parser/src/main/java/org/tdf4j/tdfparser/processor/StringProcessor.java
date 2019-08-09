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
package org.tdf4j.tdfparser.processor;

import javax.annotation.Nullable;

public class StringProcessor implements Processor<String> {

    @Override
    public String process(@Nullable final String element) {
        if(element == null || element.equals("\"\"")) {
            return "";
        }
        if(!element.matches("\"([^\"]|(\\\\\"))*\"")) {
            return element;
        }
        return element
                .substring(1, element.length() - 1)
                .replaceAll("\\\\\"", "\"");
    }

}