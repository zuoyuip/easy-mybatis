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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.JsonException;

/**
 * Json字符串处理 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 10:00
 */
public class JsonStringer {

    final StringBuilder out = new StringBuilder();
    private final List<Scope> stack = new ArrayList<>();
    /**
     * 用于单级缩进的完整空格集的字符串.
     */
    private final String indent;

    public JsonStringer() {
        this.indent = null;
    }

    JsonStringer(int indentSpaces) {
        char[] indentChars = new char[indentSpaces];
        Arrays.fill(indentChars, ' ');
        this.indent = new String(indentChars);
    }

    /**
     * 编码一个新数组，对该方法的每次调用都必须与对endArray的调用配对
     */
    public JsonStringer array() {
        return open(Scope.EMPTY_ARRAY, "[");
    }

    /**
     * 结束对当前数组的编码
     */
    public JsonStringer endArray() {
        return close(Scope.EMPTY_ARRAY, Scope.NONEMPTY_ARRAY, "]");
    }

    /**
     * 开始编码一个新对象，对该方法的每次调用都必须与对endObject的调用配对
     */
    public JsonStringer object() {
        return open(Scope.EMPTY_OBJECT, "{");
    }

    /**
     * 结束对当前对象的编码
     */
    public JsonStringer endObject() {
        return close(Scope.EMPTY_OBJECT, Scope.NONEMPTY_OBJECT, "}");
    }

    /**
     * 通过附加任何必要的空格和给定的括号来输入新的范围
     *
     * @param empty       - 必要的空白范围 {@link Scope}
     * @param openBracket - 左括号（开括号）
     */
    JsonStringer open(Scope empty, String openBracket) {
        if (this.stack.isEmpty() && this.out.length() > 0) {
            throw new JsonException("Nesting problem: multiple top-level roots");
        }
        beforeValue();
        this.stack.add(empty);
        this.out.append(openBracket);
        return this;
    }

    /**
     * 通过附加任何必要的空格和给定的括号来关闭当前范围
     *
     * @param empty        - 必要的空白范围 {@link Scope}
     * @param nonempty     - 当前范围 {@link Scope}
     * @param closeBracket - 右括号（闭括号）
     */
    JsonStringer close(Scope empty, Scope nonempty, String closeBracket) {
        Scope context = peek();
        if (context != nonempty && context != empty) {
            throw new JsonException("Nesting problem");
        }

        this.stack.remove(this.stack.size() - 1);
        if (context == nonempty) {
            newline();
        }
        this.out.append(closeBracket);
        return this;
    }

    /**
     * 返回堆栈顶部的值
     */
    private Scope peek() {
        if (this.stack.isEmpty()) {
            throw new JsonException("Nesting problem");
        }
        return this.stack.get(this.stack.size() - 1);
    }

    /**
     * 用给定的值替换堆栈顶部的值
     *
     * @param topOfStack - 给定的值（替换现有值）
     */
    private void replaceTop(Scope topOfStack) {
        this.stack.set(this.stack.size() - 1, topOfStack);
    }

    /**
     * 对 {@code value} 进行编码
     */
    public JsonStringer value(Object value) {
        if (this.stack.isEmpty()) {
            throw new JsonException("Nesting problem");
        }

        if (value instanceof JsonArray) {
            ((JsonArray) value).writeTo(this);
            return this;
        } else if (value instanceof JsonObject) {
            ((JsonObject) value).writeTo(this);
            return this;
        }

        beforeValue();

        if (value == null || value instanceof Boolean || value == JsonObject.NULL) {
            this.out.append(value);

        } else if (value instanceof Number) {
            this.out.append(JsonObject.numberToString((Number) value));

        } else {
            string(value.toString());
        }

        return this;
    }

    /**
     * 对 {@code value} 进行编码
     */
    public JsonStringer value(boolean value) {
        if (this.stack.isEmpty()) {
            throw new JsonException("Nesting problem");
        }
        beforeValue();
        this.out.append(value);
        return this;
    }

    /**
     * 对 {@code value} 进行编码
     */
    public JsonStringer value(double value) {
        if (this.stack.isEmpty()) {
            throw new JsonException("Nesting problem");
        }
        beforeValue();
        this.out.append(JsonObject.numberToString(value));
        return this;
    }

    /**
     * 对 {@code value} 进行编码
     */
    public JsonStringer value(long value) {
        if (this.stack.isEmpty()) {
            throw new JsonException("Nesting problem");
        }
        beforeValue();
        this.out.append(value);
        return this;
    }

    private void string(@NonNull String value) {
        this.out.append("\"");
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);

            /*
             * 所有 Unicode 字符都可以放在引号内，
             * 但必须转义的字符除外：引号、反斜杠和控制字符（U+0000 到 U+001F）
             */
            switch (c) {
                case '"':
                case '\\':
                case '/':
                    this.out.append('\\').append(c);
                    break;

                case '\t':
                    this.out.append("\\t");
                    break;

                case '\b':
                    this.out.append("\\b");
                    break;

                case '\n':
                    this.out.append("\\n");
                    break;

                case '\r':
                    this.out.append("\\r");
                    break;

                case '\f':
                    this.out.append("\\f");
                    break;

                default:
                    if (c <= 0x1F) {
                        this.out.append(String.format("\\u%04x", (int) c));
                    } else {
                        this.out.append(c);
                    }
                    break;
            }

        }
        this.out.append("\"");
    }

    private void newline() {
        if (this.indent == null) {
            return;
        }

        this.out.append("\n");
        for (int i = 0; i < this.stack.size(); i++) {
            this.out.append(this.indent);
        }
    }

    /**
     * 对 {@code name} 进行编码
     */
    public JsonStringer key(String name) {
        if (name == null) {
            throw new JsonException("Names must be non-null");
        }
        beforeKey();
        string(name);
        return this;
    }

    /**
     * 前置插入必要的元素（Key）
     */
    private void beforeKey() {
        Scope context = peek();
        if (context == Scope.NONEMPTY_OBJECT) {
            this.out.append(',');
        } else if (context != Scope.EMPTY_OBJECT) {
            throw new JsonException("Nesting problem");
        }
        newline();
        replaceTop(Scope.DANGLING_KEY);
    }

    /**
     * 前置插入必要的元素（Value）
     */
    private void beforeValue() {
        if (this.stack.isEmpty()) {
            return;
        }

        Scope context = peek();
        if (context == Scope.EMPTY_ARRAY) {
            replaceTop(Scope.NONEMPTY_ARRAY);
            newline();
        } else if (context == Scope.NONEMPTY_ARRAY) {
            this.out.append(',');
            newline();
        } else if (context == Scope.DANGLING_KEY) {
            this.out.append(this.indent == null ? ":" : ": ");
            replaceTop(Scope.NONEMPTY_OBJECT);
        } else if (context != Scope.NULL) {
            throw new JsonException("Nesting problem");
        }
    }

    @Override
    public String toString() {
        return this.out.length() == 0 ? null : this.out.toString();
    }

    /**
     * 字符串中的词法范围元素.
     */
    enum Scope {

        /**
         * 空数组.
         */
        EMPTY_ARRAY,

        /**
         * 非空数组.
         */
        NONEMPTY_ARRAY,

        /**
         * 空对象.
         */
        EMPTY_OBJECT,

        /**
         * An object whose most recent element is a key. The next element must be a value.
         */
        DANGLING_KEY,

        /**
         * 非空对象.
         */
        NONEMPTY_OBJECT,

        /**
         * NULL.
         */
        NULL

    }

}
