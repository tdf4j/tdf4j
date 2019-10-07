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
package io.github.tdf4j.lexer;

public class SymbolListenerImpl implements SymbolListener {
    private int column = 0;
    private int line = 1;

    SymbolListenerImpl() {
        //package-private
    }

    @Override
    public void listen(char ch) {
        if(newLine(ch)) {
            line++;
            column = 0;
        } else {
            column++;
        }
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public void reset() {
        this.line = 1;
        this.column = 0;
    }

    private boolean newLine(final char ch) {
        return ch == '\n';
    }
}
