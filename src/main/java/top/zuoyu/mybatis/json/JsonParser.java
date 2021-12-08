/*
 * MIT License
 *
 * Copyright (c) 2021 zuoyuip@foxmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package top.zuoyu.mybatis.json;


import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.JsonException;

/**
 * Json解析 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 10:00
 */
public class JsonParser {

    /**
     * Json字符串对象.
     */
    private final String in;

    /**
     * 下一个字符的索引 {@link #next}.
     */
    private int pos;

    /**
     * JSON 编码的字符串
     */
    public JsonParser(String in) {
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        this.in = in;
    }

    public static int dehexchar(char hex) {
        if (hex >= '0' && hex <= '9') {
            return hex - '0';
        } else if (hex >= 'A' && hex <= 'F') {
            return hex - 'A' + 10;
        } else if (hex >= 'a' && hex <= 'f') {
            return hex - 'a' + 10;
        } else {
            return -1;
        }
    }

    /**
     * 返回下一个值
     */
    public Object nextValue() {
        int c = nextCleanInternal();
        switch (c) {
            case -1:
                throw syntaxError("End of input");

            case '{':
                return readObject();

            case '[':
                return readArray();

            case '\'':
            case '"':
                return nextString((char) c);

            default:
                this.pos--;
                return readLiteral();
        }
    }

    private int nextCleanInternal() {
        while (this.pos < this.in.length()) {
            int c = this.in.charAt(this.pos++);
            switch (c) {
                case '\t':
                case ' ':
                case '\n':
                case '\r':
                    continue;

                case '/':
                    if (this.pos == this.in.length()) {
                        return c;
                    }

                    char peek = this.in.charAt(this.pos);
                    switch (peek) {
                        case '*':
                            this.pos++;
                            int commentEnd = this.in.indexOf("*/", this.pos);
                            if (commentEnd == -1) {
                                throw syntaxError("Unterminated comment");
                            }
                            this.pos = commentEnd + 2;
                            continue;

                        case '/':
                            this.pos++;
                            skipToEndOfLine();
                            continue;

                        default:
                            return c;
                    }

                case '#':
                    skipToEndOfLine();
                    continue;

                default:
                    return c;
            }
        }

        return -1;
    }

    /**
     * 将位置推进到下一个换行符之后
     */
    private void skipToEndOfLine() {
        for (; this.pos < this.in.length(); this.pos++) {
            char c = this.in.charAt(this.pos);
            if (c == '\r' || c == '\n') {
                this.pos++;
                break;
            }
        }
    }

    /**
     * 返回直到但不包括quote的字符串，对沿途遇到的任何字符转义序列进行转义
     * @param quote - 字符
     */
    public String nextString(char quote) {
        StringBuilder builder = null;

        int start = this.pos;

        while (this.pos < this.in.length()) {
            int c = this.in.charAt(this.pos++);
            if (c == quote) {
                if (builder == null) {
                    return this.in.substring(start, this.pos - 1);
                } else {
                    builder.append(this.in, start, this.pos - 1);
                    return builder.toString();
                }
            }

            if (c == '\\') {
                if (this.pos == this.in.length()) {
                    throw syntaxError("Unterminated escape sequence");
                }
                if (builder == null) {
                    builder = new StringBuilder();
                }
                builder.append(this.in, start, this.pos - 1);
                builder.append(readEscapeCharacter());
                start = this.pos;
            }
        }

        throw syntaxError("Unterminated string");
    }

    /**
     * 对由紧跟在反斜杠后面的一个或多个字符标识的字符取消转义
     */
    private char readEscapeCharacter() {
        char escaped = this.in.charAt(this.pos++);
        switch (escaped) {
            case 'u':
                if (this.pos + 4 > this.in.length()) {
                    throw syntaxError("Unterminated escape sequence");
                }
                String hex = this.in.substring(this.pos, this.pos + 4);
                this.pos += 4;
                return (char) Integer.parseInt(hex, 16);

            case 't':
                return '\t';

            case 'b':
                return '\b';

            case 'n':
                return '\n';

            case 'r':
                return '\r';

            case 'f':
                return '\f';

            case '\'':
            case '"':
            case '\\':
            default:
                return escaped;
        }
    }

    /**
     * 读取空值、布尔值、数字或不带引号的字符串文字值
     */
    private Object readLiteral() {
        String literal = nextToInternal("{}[]/\\:,=;# \t\f");

        if (literal.isEmpty()) {
            throw syntaxError("Expected literal value");
        } else if ("null".equalsIgnoreCase(literal)) {
            return JsonObject.NULL;
        } else if ("true".equalsIgnoreCase(literal)) {
            return Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(literal)) {
            return Boolean.FALSE;
        }

        if (literal.indexOf('.') == -1) {
            int base = 10;
            String number = literal;
            if (number.startsWith("0x") || number.startsWith("0X")) {
                number = number.substring(2);
                base = 16;
            } else if (number.startsWith("0") && number.length() > 1) {
                number = number.substring(1);
                base = 8;
            }
            try {
                long longValue = Long.parseLong(number, base);
                if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
                    return (int) longValue;
                } else {
                    return longValue;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        try {
            return Double.valueOf(literal);
        } catch (NumberFormatException ignored) {
        }

        return literal;
    }

    /**
     * 返回直到但不包括任何给定字符或换行符的字符串
     * @param excluded - 给定字符
     */
    @NonNull
    private String nextToInternal(@NonNull String excluded) {
        int start = this.pos;
        for (; this.pos < this.in.length(); this.pos++) {
            char c = this.in.charAt(this.pos);
            if (c == '\r' || c == '\n' || excluded.indexOf(c) != -1) {
                return this.in.substring(start, this.pos);
            }
        }
        return this.in.substring(start);
    }

    /**
     * 读取对象的键/值对序列和结尾的右大括号“}”
     */
    private JsonObject readObject() {
        JsonObject result = new JsonObject();

        int first = nextCleanInternal();
        if (first == '}') {
            return result;
        } else if (first != -1) {
            this.pos--;
        }

        while (true) {
            Object name = nextValue();
            if (!(name instanceof String)) {
                if (name == null) {
                    throw syntaxError("Names cannot be null");
                } else {
                    throw syntaxError(
                            "Names must be strings, but " + name + " is of type " + name.getClass().getName());
                }
            }

            int separator = nextCleanInternal();
            if (separator != ':' && separator != '=') {
                throw syntaxError("Expected ':' after " + name);
            }
            if (this.pos < this.in.length() && this.in.charAt(this.pos) == '>') {
                this.pos++;
            }

            result.put((String) name, nextValue());

            switch (nextCleanInternal()) {
                case '}':
                    return result;
                case ';':
                case ',':
                    continue;
                default:
                    throw syntaxError("Unterminated object");
            }
        }
    }

    /**
     * 读取值序列和数组的尾部右括号“]”
     */
    private JsonArray readArray() {
        JsonArray result = new JsonArray();

        boolean hasTrailingSeparator = false;

        while (true) {
            switch (nextCleanInternal()) {
                case -1:
                    throw syntaxError("Unterminated array");
                case ']':
                    if (hasTrailingSeparator) {
                        result.put(null);
                    }
                    return result;
                case ',':
                case ';':
                    result.put(null);
                    hasTrailingSeparator = true;
                    continue;
                default:
                    this.pos--;
            }

            result.put(nextValue());

            switch (nextCleanInternal()) {
                case ']':
                    return result;
                case ',':
                case ';':
                    hasTrailingSeparator = true;
                    continue;
                default:
                    throw syntaxError("Unterminated array");
            }
        }
    }

    /**
     * 返回包含给定消息加上当前位置和整个输入字符串的异常
     */
    public JsonException syntaxError(String message) {
        return new JsonException(message + this);
    }

    @Override
    public String toString() {
        return " at character " + this.pos + " of " + this.in;
    }

    public boolean more() {
        return this.pos < this.in.length();
    }

    public char next() {
        return this.pos < this.in.length() ? this.in.charAt(this.pos++) : '\0';
    }

    public char next(char c) throws JsonException {
        char result = next();
        if (result != c) {
            throw syntaxError("Expected " + c + " but was " + result);
        }
        return result;
    }

    public char nextClean() throws JsonException {
        int nextCleanInt = nextCleanInternal();
        return nextCleanInt == -1 ? '\0' : (char) nextCleanInt;
    }

    public String next(int length) throws JsonException {
        if (this.pos + length > this.in.length()) {
            throw syntaxError(length + " is out of bounds");
        }
        String result = this.in.substring(this.pos, this.pos + length);
        this.pos += length;
        return result;
    }

    public String nextTo(String excluded) {
        if (excluded == null) {
            throw new NullPointerException("excluded == null");
        }
        return nextToInternal(excluded).trim();
    }

    public String nextTo(char excluded) {
        return nextToInternal(String.valueOf(excluded)).trim();
    }

    public void skipPast(String thru) {
        int thruStart = this.in.indexOf(thru, this.pos);
        this.pos = thruStart == -1 ? this.in.length() : (thruStart + thru.length());
    }

    public char skipTo(char to) {
        int index = this.in.indexOf(to, this.pos);
        if (index != -1) {
            this.pos = index;
            return to;
        } else {
            return '\0';
        }
    }

    public void back() {
        if (--this.pos == -1) {
            this.pos = 0;
        }
    }

}
