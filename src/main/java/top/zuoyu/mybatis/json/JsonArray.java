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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.RandomAccess;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.JsonException;
import top.zuoyu.mybatis.json.convert.BigDecimalConvert;
import top.zuoyu.mybatis.json.convert.BigIntegerConvert;
import top.zuoyu.mybatis.json.convert.BooleanConvert;
import top.zuoyu.mybatis.json.convert.ByteConvert;
import top.zuoyu.mybatis.json.convert.CharacterConvert;
import top.zuoyu.mybatis.json.convert.DateConvert;
import top.zuoyu.mybatis.json.convert.DoubleConvert;
import top.zuoyu.mybatis.json.convert.FloatConvert;
import top.zuoyu.mybatis.json.convert.IntegerConvert;
import top.zuoyu.mybatis.json.convert.LongConvert;
import top.zuoyu.mybatis.json.convert.ShortConvert;
import top.zuoyu.mybatis.json.convert.StringConvert;

/**
 * Json数组 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 10:00
 */
public class JsonArray implements List<Object>, Cloneable, RandomAccess, Serializable {

    private static final long serialVersionUID = -261413003600010487L;
    private final List<Object> values;

    public JsonArray() {
        this.values = new ArrayList<Object>();
    }

    public JsonArray(int size) {
        this.values = new ArrayList<Object>(size);
    }

    /**
     * 通过复制给定集合中的所有值来创建一个新的JsonArray
     *
     * @param copyFrom – 一个集合
     */
    @SuppressWarnings("rawtypes")
    public JsonArray(@NonNull Collection copyFrom) {
        this(copyFrom.size());
        for (Object o : copyFrom) {
            put(JsonObject.wrap(o));
        }
    }

    /**
     * 使用标记器中下一个数组的值创建一个新的JsonArray
     *
     * @param readFrom - 标记器
     */
    public JsonArray(@NonNull JsonParser readFrom) {
        Object object = readFrom.nextValue();
        if (object instanceof JsonArray) {
            this.values = ((JsonArray) object).values;
        } else {
            throw Json.typeMismatch(object, "JSONArray");
        }
    }

    /**
     * 使用来自 Json 字符串的值创建一个新的JsonArray
     *
     * @param json - JSON 字符串
     */
    public JsonArray(String json) {
        this(new JsonParser(json));
    }

    /**
     * 使用给定原始数组中的值创建一个新的JsonArray
     *
     * @param array - 给定数组
     */
    public JsonArray(@NonNull Object array) {
        if (!array.getClass().isArray()) {
            throw new JsonException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        this.values = new ArrayList<Object>(length);
        for (int i = 0; i < length; ++i) {
            put(JsonObject.wrap(Array.get(array, i)));
        }
    }

    /**
     * 使用给定原始数组中的值创建一个新的JsonArray
     *
     * @param array - 给定数组
     */
    public JsonArray(@NonNull Object[] array) {
        this(Array.getLength(array));
        for (int i = 0; i < Array.getLength(array); ++i) {
            put(JsonObject.wrap(array[i]));
        }
    }

    /**
     * 数组长度
     *
     * @return 数组长度
     */
    public int length() {
        return this.values.size();
    }

    /**
     * 将value附加到此数组的末尾
     *
     * @param value the value
     * @return {@link JsonArray}
     */
    public JsonArray put(boolean value) {
        this.values.add(value);
        return this;
    }

    /**
     * 将value附加到此数组的末尾
     *
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(double value) {
        this.values.add(Json.checkDouble(value));
        return this;
    }

    /**
     * 将value附加到此数组的末尾
     *
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int value) {
        this.values.add(value);
        return this;
    }

    /**
     * 将value附加到此数组的末尾
     *
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(long value) {
        this.values.add(value);
        return this;
    }

    /**
     * 将value附加到此数组的末尾
     *
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(Object value) {
        this.values.add(value);
        return this;
    }

    /**
     * 将index处的value设置为value ，如有必要，将此数组空填充到所需的长度。
     * 如果一个值已经存在于index ，它将被替换
     *
     * @param index - 给定索引
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int index, boolean value) {
        return put(index, (Boolean) value);
    }

    /**
     * 将index处的value设置为value ，如有必要，将此数组空填充到所需的长度。
     * 如果一个值已经存在于index ，它将被替换
     *
     * @param index - 给定索引
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int index, double value) {
        return put(index, (Double) value);
    }

    /**
     * 将index处的value设置为value ，如有必要，将此数组空填充到所需的长度。
     * 如果一个值已经存在于index ，它将被替换
     *
     * @param index - 给定索引
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int index, int value) {
        return put(index, (Integer) value);
    }

    /**
     * 将index处的value设置为value ，如有必要，将此数组空填充到所需的长度。
     * 如果一个值已经存在于index ，它将被替换
     *
     * @param index - 给定索引
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int index, long value) {
        return put(index, (Long) value);
    }

    /**
     * 将index处的value设置为value ，如有必要，将此数组空填充到所需的长度。
     * 如果一个值已经存在于index ，它将被替换
     *
     * @param index - 给定索引
     * @param value - the value
     * @return {@link JsonArray}
     */
    public JsonArray put(int index, Object value) {
        if (value instanceof Number) {
            Json.checkDouble(((Number) value).doubleValue());
        }
        while (this.values.size() <= index) {
            this.values.add(null);
        }
        this.values.set(index, value);
        return this;
    }

    /**
     * 如果此数组在此索引处没有值，或者其值为null引用或JsonObject.NULL ，则返回 true
     *
     * @param index - 索引
     * @return boolean
     */
    public boolean isNull(int index) {
        Object value = opt(index);
        return Objects.isNull(value) || value == JsonObject.NULL;
    }

    /**
     * 返回index索引的值
     *
     * @param index - 索引
     * @return 对应的值
     */
    @Override
    public Object get(int index) {
        try {
            Object value = this.values.get(index);
            if (Objects.isNull(value)) {
                throw new JsonException("Value at " + index + " is null.");
            }
            return value;
        } catch (IndexOutOfBoundsException e) {
            throw new JsonException("Index " + index + " out of range [0.." + this.values.size() + ")");
        }
    }

    @Override
    public Object set(int index, Object element) {
        return this.values.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        this.values.add(index, element);
    }

    /**
     * 返回index索引的值，如果数组在index处没有值，则返回 null
     *
     * @param index - 索引
     * @return 对应的值
     */
    public Object opt(int index) {
        if (index < 0 || index >= this.values.size()) {
            return null;
        }
        return this.values.get(index);
    }

    /**
     * 删除并返回index处的值，如果数组在index处没有值，则返回 null
     *
     * @param index - 索引
     * @return 对应的值
     */
    @Override
    public Object remove(int index) {
        if (index < 0 || index >= this.values.size()) {
            return null;
        }
        return this.values.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.values.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.values.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return this.values.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return this.values.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return this.values.subList(fromIndex, toIndex);
    }

    /**
     * 返回index索引的值为 {@link Boolean}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Boolean getBoolean(int index) {
        Object object = get(index);
        Boolean result = Json.toBoolean(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "boolean");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BooleanConvert} 进行值的转换 ）
     *
     * @param index          - 索引
     * @param booleanConvert - Boolean转换器
     */
    public Boolean getBoolean(int index, @NonNull BooleanConvert booleanConvert) {
        Object object = get(index);
        return booleanConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BooleanConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index          - 索引
     * @param booleanConvert - Boolean转换器
     * @param defaultValue   - 默认值
     */
    public Boolean getBoolean(int index, @NonNull BooleanConvert booleanConvert, Boolean defaultValue) {
        Object object = get(index);
        return booleanConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值（如果它存在并且是布尔值或可以强制为布尔值）,否则返回 false
     *
     * @param index - 索引
     * @return 对应的值
     */
    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    /**
     * 返回index索引的值（如果它存在并且是布尔值或可以强制为布尔值）,否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public boolean optBoolean(int index, boolean fallback) {
        Object object = opt(index);
        Boolean result = Json.toBoolean(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Byte}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Byte getByte(int index) {
        Object object = get(index);
        Byte result = Json.toByte(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "byte");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link ByteConvert} 进行值的转换 ）
     *
     * @param index       - 索引
     * @param byteConvert - Byte转换器
     */
    public Byte getByte(int index, @NonNull ByteConvert byteConvert) {
        Object object = get(index);
        return byteConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link ByteConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param byteConvert  - Byte转换器
     * @param defaultValue - 默认值
     */
    public Byte getByte(int index, @NonNull ByteConvert byteConvert, Byte defaultValue) {
        Object object = get(index);
        return byteConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是byte值或可以强制为byte值），否则返回 0
     *
     * @param index - 索引
     * @return 值或 0
     */
    public byte optByte(int index) {
        return optByte(index, (byte) 0);
    }

    /**
     * 返回index索引的值，（如果它存在并且是byte值或可以强制为byte值），否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public byte optByte(int index, byte fallback) {
        Object object = opt(index);
        Byte result = Json.toByte(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Short}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Short getShort(int index) {
        Object object = get(index);
        Short result = Json.toShort(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "short");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link ShortConvert} 进行值的转换 ）
     *
     * @param index        - 索引
     * @param shortConvert - Short转换器
     */
    public Short getShort(int index, @NonNull ShortConvert shortConvert) {
        Object object = get(index);
        return shortConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link ShortConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param shortConvert - Short转换器
     * @param defaultValue - 默认值
     */
    public Short getShort(int index, @NonNull ShortConvert shortConvert, Short defaultValue) {
        Object object = get(index);
        return shortConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是short值或可以强制为short值），否则返回 0
     *
     * @param index - 索引
     * @return 值或 NaN
     */
    public short optShort(int index) {
        return optShort(index, (short) 0);
    }

    /**
     * 返回index索引的值，（如果它存在并且是short值或可以强制为short值），否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public Short optShort(int index, short fallback) {
        Object object = opt(index);
        Short result = Json.toShort(object);
        return result != null ? result : fallback;
    }


    /**
     * 返回index索引的值为 {@link Integer}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Integer getInteger(int index) {
        Object object = get(index);
        Integer result = Json.toInteger(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "int");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link IntegerConvert} 进行值的转换 ）
     *
     * @param index          - 索引
     * @param integerConvert - Integer转换器
     */
    public Integer getInteger(int index, @NonNull IntegerConvert integerConvert) {
        Object object = get(index);
        return integerConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link IntegerConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index          - 索引
     * @param integerConvert - Integer转换器
     * @param defaultValue   - 默认值
     */
    public Integer getInteger(int index, @NonNull IntegerConvert integerConvert, Integer defaultValue) {
        Object object = get(index);
        return integerConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值（如果它存在并且是一个 int 或者可以被强制转换为一个 int）,否则返回 0
     *
     * @param index - 索引
     * @return 对应的值或 0
     */
    public int optInt(int index) {
        return optInt(index, 0);
    }

    /**
     * 返回index索引的值（如果它存在并且是一个 int 或者可以被强制转换为一个 int）,否则返回否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public int optInt(int index, int fallback) {
        Object object = opt(index);
        Integer result = Json.toInteger(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Long}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Long getLong(int index) {
        Object object = get(index);
        Long result = Json.toLong(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "long");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link LongConvert} 进行值的转换 ）
     *
     * @param index       - 索引
     * @param longConvert - Long转换器
     */
    public Long getLong(int index, @NonNull LongConvert longConvert) {
        Object object = get(index);
        return longConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link LongConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param longConvert  - Long转换器
     * @param defaultValue - 默认值
     */
    public Long getLong(int index, @NonNull LongConvert longConvert, Long defaultValue) {
        Object object = get(index);
        return longConvert.convert(object, defaultValue);
    }


    /**
     * 返回index索引的值（如果它存在并且是一个 long 或者可以被强制转换为一个 long）,否则返回 0
     *
     * @param index - 索引
     * @return 对应的值或 0
     */
    public long optLong(int index) {
        return optLong(index, 0L);
    }

    /**
     * 返回index索引的值（如果它存在并且是一个 long 或者可以被强制转换为一个 long）,否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public long optLong(int index, long fallback) {
        Object object = opt(index);
        Long result = Json.toLong(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Float}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Float getFloat(int index) {
        Object object = get(index);
        Float result = Json.toFloat(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "float");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link FloatConvert} 进行值的转换 ）
     *
     * @param index        - 索引
     * @param floatConvert - Float转换器
     */
    public Float getFloat(int index, @NonNull FloatConvert floatConvert) {
        Object object = get(index);
        return floatConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link FloatConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param floatConvert - Float转换器
     * @param defaultValue - 默认值
     */
    public Float getFloat(int index, @NonNull FloatConvert floatConvert, Float defaultValue) {
        Object object = get(index);
        return floatConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是一个 float 或可以强制为一个 float ），否则返回 0
     *
     * @param index - 索引
     * @return 值或 0
     */
    public float optFloat(int index) {
        return optFloat(index, 0F);
    }

    /**
     * 返回index索引的值，（如果它存在并且是一个 float 或可以强制为一个 float ），否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public float optFloat(int index, float fallback) {
        Object object = opt(index);
        Float result = Json.toFloat(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Double}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Double getDouble(int index) {
        Object object = get(index);
        Double result = Json.toDouble(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "double");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DoubleConvert} 进行值的转换 ）
     *
     * @param index         - 索引
     * @param doubleConvert - Double转换器
     */
    public Double getDouble(int index, @NonNull DoubleConvert doubleConvert) {
        Object object = get(index);
        return doubleConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DoubleConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index         - 索引
     * @param doubleConvert - Double转换器
     * @param defaultValue  - 默认值
     */
    public Double getDouble(int index, @NonNull DoubleConvert doubleConvert, Double defaultValue) {
        Object object = get(index);
        return doubleConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值（如果它存在并且是双精度值或可以强制为双精度值）,否则返回 NaN
     *
     * @param index - 索引
     * @return 对应的值
     */
    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    /**
     * 返回index索引的值（如果它存在并且是双精度值或可以强制为双精度值）,否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public double optDouble(int index, double fallback) {
        Object object = opt(index);
        Double result = Json.toDouble(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link Character}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Character getCharacter(int index) {
        Object object = get(index);
        Character result = Json.toCharacter(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "char");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link CharacterConvert} 进行值的转换 ）
     *
     * @param index            - 索引
     * @param characterConvert - Character转换器
     */
    public Character getCharacter(int index, @NonNull CharacterConvert characterConvert) {
        Object object = get(index);
        return characterConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link CharacterConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index            - 索引
     * @param characterConvert - Character转换器
     * @param defaultValue     - 默认值
     */
    public Character getCharacter(int index, @NonNull CharacterConvert characterConvert, Character defaultValue) {
        Object object = get(index);
        return characterConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是char值或可以强制为char值），否则返回 '\u0000'（即空格）
     *
     * @param index - 索引
     * @return 值或 NaN
     */
    public char optCharacter(int index) {
        return optCharacter(index, Character.MIN_VALUE);
    }

    /**
     * 返回index索引的值，（如果它存在并且是char值或可以强制为char值），否则返回 {@code fallback}
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public char optCharacter(int index, char fallback) {
        Object object = opt(index);
        Character result = Json.toCharacter(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值为 {@link String}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果该值不存在
     */
    public String getString(int index) {
        Object object = get(index);
        String result = Json.toString(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "String");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link StringConvert} 进行值的转换 ）
     *
     * @param index         - 索引
     * @param stringConvert - String转换器
     */
    public String getString(int index, @NonNull StringConvert stringConvert) {
        Object object = get(index);
        return stringConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link StringConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index         - 索引
     * @param stringConvert - String转换器
     * @param defaultValue  - 默认值
     */
    public String getString(int index, @NonNull StringConvert stringConvert, String defaultValue) {
        Object object = get(index);
        return stringConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值（必要时对其进行强制转换。 如果不存在这样的值，则返回空字符串）
     *
     * @param index - 索引
     * @return 对应的值或空字符串
     */
    public String optString(int index) {
        return optString(index, "");
    }

    /**
     * 返回index索引的值（必要时对其进行强制转换。 如果不存在这样的值，则返回 {@code fallback}）
     *
     * @param index    - 索引
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public String optString(int index, String fallback) {
        Object object = opt(index);
        String result = Json.toString(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回index索引的值，（如果它存在并且是 {@link BigDecimal} 或可以强制为 {@link BigDecimal} ）
     *
     * @param index - 索引
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public BigDecimal getBigDecimal(int index) {
        Object object = get(index);
        BigDecimal result = Json.toBigDecimal(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "BigDecimal");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BigDecimalConvert} 进行值的转换 ）
     *
     * @param index             - 索引
     * @param bigDecimalConvert - BigDecimal转换器
     */
    public BigDecimal getBigDecimal(int index, @NonNull BigDecimalConvert bigDecimalConvert) {
        Object object = get(index);
        return bigDecimalConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BigDecimalConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index             - 索引
     * @param bigDecimalConvert - BigDecimal转换器
     * @param defaultValue      - 默认值
     */
    public BigDecimal getBigDecimal(int index, @NonNull BigDecimalConvert bigDecimalConvert, BigDecimal defaultValue) {
        Object object = get(index);
        return bigDecimalConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是 {@link BigInteger} 或可以强制为 {@link BigInteger} ）
     *
     * @param index - 索引
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public BigInteger getBigInteger(int index) {
        Object object = get(index);
        BigInteger result = Json.toBigInteger(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "BigInteger");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BigIntegerConvert} 进行值的转换 ）
     *
     * @param index             - 索引
     * @param bigIntegerConvert - BigInteger转换器
     */
    public BigInteger getBigInteger(int index, @NonNull BigIntegerConvert bigIntegerConvert) {
        Object object = get(index);
        return bigIntegerConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link BigIntegerConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index             - 索引
     * @param bigIntegerConvert - BigInteger转换器
     * @param defaultValue      - 默认值
     */
    public BigInteger getBigInteger(int index, @NonNull BigIntegerConvert bigIntegerConvert, BigInteger defaultValue) {
        Object object = get(index);
        return bigIntegerConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是 {@link Date} 或可以强制为 {@link Date} ）
     *
     * @param index - 索引
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Date getDate(int index) {
        Object object = get(index);
        Date result = Json.toDate(object);
        if (result == null) {
            throw Json.typeMismatch(index, object, "Date");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）
     *
     * @param index       - 索引
     * @param dateConvert - Date解析器
     */
    public Date getDate(int index, @NonNull DateConvert dateConvert) {
        Object object = get(index);
        return dateConvert.convert(object);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param dateConvert  - Date解析器
     * @param defaultValue - 默认值
     */
    public Date getDate(int index, @NonNull DateConvert dateConvert, Date defaultValue) {
        Object object = get(index);
        return dateConvert.convert(object, defaultValue);
    }

    /**
     * 返回index索引的值，（如果它存在并且是 {@link Date} 或可以强制为 {@link Date} ）
     *
     * @param index  - 索引
     * @param format - 解析格式
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Date getDate(int index, String format) {
        Object object = get(index);
        Date result = Json.toDate(object, format);
        if (result == null) {
            throw Json.typeMismatch(index, object, "Date");
        }
        return result;
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）
     *
     * @param index       - 索引
     * @param format      - 解析格式
     * @param dateConvert - Date解析器
     */
    public Date getDate(int index, String format, @NonNull DateConvert dateConvert) {
        Object object = get(index);
        return dateConvert.convert(object, format);
    }

    /**
     * 返回index索引的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param index        - 索引
     * @param format       - 解析格式
     * @param dateConvert  - Date解析器
     * @param defaultValue - 默认值
     */
    public Date getDate(int index, String format, @NonNull DateConvert dateConvert, Date defaultValue) {
        Object object = get(index);
        return dateConvert.convert(object, format, defaultValue);
    }

    /**
     * 返回index索引的值为 {@link JsonArray}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public JsonArray getJsonArray(int index) {
        Object object = get(index);
        if (object instanceof JsonArray) {
            return (JsonArray) object;
        } else {
            throw Json.typeMismatch(index, object, "JSONArray");
        }
    }

    /**
     * 返回index索引的值（如果它存在并且是JSONArray）,否则返回 null
     *
     * @param index - 索引
     * @return 对应的值
     */
    public JsonArray optJsonArray(int index) {
        Object object = opt(index);
        return object instanceof JsonArray ? (JsonArray) object : null;
    }

    /**
     * 返回index索引的值为 {@link JsonObject}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public JsonObject getJsonObject(int index) {
        Object object = get(index);
        if (object instanceof JsonObject) {
            return (JsonObject) object;
        } else {
            throw Json.typeMismatch(index, object, "JSONObject");
        }
    }

    /**
     * 返回index索引的值（如果它存在并且是JsonObject）,否则返回 null
     *
     * @param index - 索引
     * @return 对应的值
     */
    public JsonObject optJsonObject(int index) {
        Object object = opt(index);
        return object instanceof JsonObject ? (JsonObject) object : null;
    }

    /**
     * 返回一个新的对象，其值是该数组中他们的名字为 names 的值 。
     * 名称和值按从 0 到较短数组长度的索引配对。 不是字符串的名称将被强制为字符串。
     * 如果任一数组为空，则此方法返回 null
     *
     * @param names 名称组
     * @return {@link JsonObject} 格式的结果
     */
    public JsonObject toJsonObject(@NonNull JsonArray names) {
        JsonObject result = new JsonObject();
        int length = Math.min(names.length(), this.values.size());
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            String name = Json.toString(names.opt(i));
            result.put(name, opt(i));
        }
        return result;
    }

    /**
     * 转换为 {@link JsonObject} 集合
     *
     * @return - 对应的 {@link JsonObject} 集合
     * @throws JsonException - 元素转换异常
     */
    @SuppressWarnings("rawtypes")
    public List<JsonObject> toJsonObjects() {
        if (this.values.size() < 1) {
            return null;
        }
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Object value : this.values) {
            if (value instanceof JsonObject) {
                jsonObjects.add((JsonObject) value);
                continue;
            }
            if (value instanceof Map) {
                jsonObjects.add(new JsonObject((Map) value));
                continue;
            }
            if (value instanceof JsonParser) {
                jsonObjects.add(new JsonObject((JsonParser) value));
                continue;
            }
            if (value instanceof String) {
                jsonObjects.add(new JsonObject((String) value));
            }
        }
        return jsonObjects;
    }

    /**
     * 通过将此数组的值与 {@code separator} 交替来返回一个新字符串。该数组的字符串值被引用并转义它们的特殊字符。
     * 例如 <pre>["小喵喵", "正在", "喝水"]<pre>，如果 {@code separator} 的值为 "+"
     * 结果为<pre>"小喵喵正在喝水"</pre>
     * @param separator 转义字符
     * @return 转义的字符串
     */
    public String join(String separator) {
        JsonStringer stringer = new JsonStringer();
        stringer.open(JsonStringer.Scope.NULL, "");
        for (int i = 0, size = this.values.size(); i < size; i++) {
            if (i > 0) {
                stringer.out.append(separator);
            }
            stringer.value(this.values.get(i));
        }
        stringer.close(JsonStringer.Scope.NULL, JsonStringer.Scope.NULL, "");
        return stringer.out.toString();
    }

    @Override
    public String toString() {
        try {
            JsonStringer stringer = new JsonStringer();
            writeTo(stringer);
            return stringer.toString();
        } catch (JsonException e) {
            return null;
        }
    }

    /**
     * Json字符串格式化（美化）
     *
     * @param indentSpaces – 每级嵌套缩进的空格数
     */
    public String toString(int indentSpaces) {
        JsonStringer stringer = new JsonStringer(indentSpaces);
        writeTo(stringer);
        return stringer.toString();
    }

    void writeTo(@NonNull JsonStringer stringer) {
        stringer.array();
        for (Object value : this.values) {
            stringer.value(value);
        }
        stringer.endArray();
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.values.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return this.values.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.values.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return this.values.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return this.values.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.values.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return this.values.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return this.values.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.values.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.values.retainAll(c);
    }

    @Override
    public void clear() {
        this.values.clear();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JsonArray && ((JsonArray) o).values.equals(this.values);
    }

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

}
