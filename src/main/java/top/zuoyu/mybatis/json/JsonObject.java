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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.autoconfigure.EasyProperties;
import top.zuoyu.mybatis.exception.JsonException;
import top.zuoyu.mybatis.json.convert.BigDecimalConvert;
import top.zuoyu.mybatis.json.convert.BigIntegerConvert;
import top.zuoyu.mybatis.json.convert.BooleanConvert;
import top.zuoyu.mybatis.json.convert.ByteConvert;
import top.zuoyu.mybatis.json.convert.CharacterConvert;
import top.zuoyu.mybatis.json.convert.ConvertClass;
import top.zuoyu.mybatis.json.convert.DateConvert;
import top.zuoyu.mybatis.json.convert.DoubleConvert;
import top.zuoyu.mybatis.json.convert.FloatConvert;
import top.zuoyu.mybatis.json.convert.IntegerConvert;
import top.zuoyu.mybatis.json.convert.LongConvert;
import top.zuoyu.mybatis.json.convert.ShortConvert;
import top.zuoyu.mybatis.json.convert.StringConvert;
import top.zuoyu.mybatis.utils.DateUtils;

/**
 * Json对象 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 10:00
 */
public class JsonObject implements Cloneable, Serializable, Map<String, Object> {

    /**
     * 用于显式定义没有值的名称的标记值
     */
    public static final Object NULL = new Object() {

        @Override
        public boolean equals(Object o) {
            return o == this || o == null;
        }

        @Override
        @NonNull
        public String toString() {
            return "null";
        }

    };
    private static final Double NEGATIVE_ZERO = -0d;
    private static final long serialVersionUID = 1L;
    private final Map<String, Object> nameValuePairs;

    public JsonObject() {
        this.nameValuePairs = new LinkedHashMap<String, Object>();
    }

    /**
     * 通过从给定映射复制所有名称/值映射来创建一个新的JsonObject
     *
     * @param copyFrom – 键为String类型且值为受支持类型的映射
     */
    @SuppressWarnings("rawtypes")
    public JsonObject(Map copyFrom) {
        this();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) copyFrom).entrySet()) {
            String key = (String) entry.getKey();
            if (Objects.isNull(key)) {
                throw new NullPointerException("key is null");
            }
            this.nameValuePairs.put(key, wrap(entry.getValue()));
        }
    }


    /**
     * 使用标记器中下一个对象的名称/值映射创建一个新的JsonObject
     *
     * @param readFrom – 一个标记器，用 nextValue() 方法的值生成一个JsonObject
     */
    public JsonObject(@NonNull JsonParser readFrom) {
        Object object = readFrom.nextValue();
        if (object instanceof JsonObject) {
            this.nameValuePairs = ((JsonObject) object).nameValuePairs;
        } else {
            throw Json.typeMismatch(object, "JSONObject");
        }
    }

    /**
     * 使用来自 JSON 字符串的名称/值映射创建一个新的JsonObject
     *
     * @param json – 包含对象的 JSON 编码字符串
     */
    public JsonObject(String json) {
        this(new JsonParser(json));
    }

    /**
     * 通过从给定对象复制列出的名称的映射，创建一个新的JsonObject，
     * copyFrom中不存在的copyFrom将被跳过
     *
     * @param copyFrom – 来源
     * @param names    - 属性名称
     */
    public JsonObject(@NonNull JsonObject copyFrom, @NonNull String[] names) {
        this();
        for (String name : names) {
            Object value = copyFrom.opt(name);
            if (value != null) {
                this.nameValuePairs.put(name, value);
            }
        }
    }

    /**
     * 通过Bean对象创建创建一个新的JsonObject，
     *
     * @param bean - 对象
     */
    public JsonObject(Object bean) {
        this();
        if (Objects.nonNull(bean)) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                Object value = beanMap.get(key);
                this.nameValuePairs.put(key.toString(), value);
            }
        }
    }

    /**
     * 将数字转化为 JSON 字符串
     *
     * @param number - 数字
     */
    public static String numberToString(Number number) {
        if (number == null) {
            throw new JsonException("Number must be non-null");
        }

        double doubleValue = number.doubleValue();
        Json.checkDouble(doubleValue);

        if (number.equals(NEGATIVE_ZERO)) {
            return "-0";
        }

        long longValue = number.longValue();
        if (doubleValue == longValue) {
            return Long.toString(longValue);
        }

        return number.toString();
    }

    /**
     * 将字符串编码为 JSON 字符串。 这适用于引号和任何必要的字符转义
     *
     * @param data - 字符串
     */
    public static String quote(String data) {
        if (data == null) {
            return "\"\"";
        }
        try {
            JsonStringer stringer = new JsonStringer();
            stringer.open(JsonStringer.Scope.NULL, "");
            stringer.value(data);
            stringer.close(JsonStringer.Scope.NULL, JsonStringer.Scope.NULL, "");
            return stringer.toString();
        } catch (JsonException e) {
            throw new AssertionError();
        }
    }

    /**
     * 包装给定的对象。
     * 如果对象为 null 或 ，则返回NULL
     * 如果对象是JSONArray或JSONObject ，则不需要包装
     * 如果对象为NULL ，则不需要包装
     * 如果对象是数组或Collection ，则返回等效的JSONArray
     * 如果对象是Map ，则返回等效的JSONObject
     * 如果对象是原始包装器类型或String ，则返回该对象
     * 否则，如果对象来自java包，则返回toString的结果。 如果包装失败，则返回 null
     *
     * @param o - 给定的对象
     */
    @SuppressWarnings("rawtypes")
    public static Object wrap(Object o) {
        if (Objects.isNull(o)) {
            return NULL;
        }
        if (o instanceof JsonArray || o instanceof JsonObject) {
            return o;
        }
        if (o.equals(NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JsonArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return new JsonArray(o);
            }
            if (o instanceof Map) {
                return new JsonObject((Map) o);
            }
            if (o instanceof Boolean || o instanceof Byte || o instanceof Character || o instanceof Double
                    || o instanceof Float || o instanceof Integer || o instanceof Long || o instanceof Short
                    || o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 对指定键进行重命名，不破坏键值关系
     *
     * @param oldKey - 指定键名称
     * @param newKey – 赋予的新键名称
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject renameKey(@NonNull String oldKey, @NonNull String newKey) {
        if (this.nameValuePairs.containsKey(oldKey)) {
            if (this.nameValuePairs.containsKey(newKey)) {
                throw new JsonException("this " + newKey + " is exists");
            }
            Object value = this.nameValuePairs.get(oldKey);
            this.nameValuePairs.put(newKey, value);
            this.nameValuePairs.remove(oldKey, value);
        }
        return this;
    }


    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, boolean value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, double value) {
        this.nameValuePairs.put(checkName(name), Json.checkDouble(value));
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, int value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, long value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, byte value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, short value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, char value) {
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, String value) {
        try {
            this.nameValuePairs.put(checkName(name), value);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, @NonNull Date value) {
        String format = Objects.isNull(EasyProperties.getDateFormat()) ? "yyyy-MM-dd HH:mm:ss" : EasyProperties.getDateFormat();
        String s = new SimpleDateFormat(format).format(value);
        try {
            this.nameValuePairs.put(checkName(name), s);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, @NonNull Calendar value) {
        Date date = value.getTime();
        return put(name, date);
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject put(@NonNull String name, @NonNull TemporalAccessor value) {
        LocalDateTime localDateTime = LocalDateTime.from(value);
        return put(name, DateUtils.asDate(localDateTime));
    }

    /**
     * 返回此对象中名称/值映射的数量
     */
    @Override
    public int size() {
        return this.nameValuePairs.size();
    }

    /**
     * 判断此对象是否为空
     *
     * @return 是/否
     */
    @Override
    public boolean isEmpty() {
        return this.nameValuePairs.isEmpty();
    }

    /**
     * 判断此对象是否存在该名称
     *
     * @param key - 名称
     * @return 是/否
     */
    @Override
    public boolean containsKey(Object key) {
        return this.nameValuePairs.containsKey(key);
    }

    /**
     * 判断此对象是否存在该zhi值
     *
     * @param value - 值
     * @return 是/否
     */
    @Override
    public boolean containsValue(Object value) {
        return this.nameValuePairs.containsValue(value);
    }

    /**
     * 获取此对象中名称为{@code key}的值
     *
     * @param key - 名称
     * @return 对应的值
     */
    @Override
    public Object get(Object key) {
        return this.nameValuePairs.get(key);
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @Override
    @NonNull
    public JsonObject put(@NonNull String name, Object value) {
        if (value == null) {
            this.nameValuePairs.remove(name);
            return this;
        }
        if (value instanceof Number) {
            try {
                Json.checkDouble(((Number) value).doubleValue());
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }
        try {
            this.nameValuePairs.put(checkName(name), value);
        } catch (JsonException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 如果存在，则删除对应映射
     *
     * @param key - 属性的名称
     * @return 先前由name映射的值，如果没有这样的映射，则为 null
     */
    @Override
    public Object remove(@NonNull Object key) {
        return this.nameValuePairs.remove(key);
    }

    /**
     * 添加相应Map的所有名称和映射
     *
     * @param map - Map
     */
    @Override
    public void putAll(Map<? extends String, ?> map) {
        this.nameValuePairs.putAll(map);
    }

    /**
     * 清空所有名称和映射
     */
    @Override
    public void clear() {
        this.nameValuePairs.clear();
    }

    /**
     * 仅保留指定的键值对
     *
     * @param keys - 保留的键
     * @return {@link JsonObject}
     */
    public JsonObject includes(@NonNull String... keys) {
        List<String> keyList = Arrays.asList(keys);
        for (Entry<String, Object> entry : this.nameValuePairs.entrySet()) {
            if (keyList.contains(entry.getKey())) {
                continue;
            }
            this.remove(entry.getKey());
        }
        return this;
    }

    /**
     * 排除指定的键值对，仅保留剩余的
     *
     * @param keys - 排除的键
     * @return {@link JsonObject}
     */
    public JsonObject exclusions(@NonNull String... keys) {
        List<String> keyList = Arrays.asList(keys);
        for (Entry<String, Object> entry : this.nameValuePairs.entrySet()) {
            if (keyList.contains(entry.getKey())) {
                this.remove(entry.getKey());
            }
        }
        return this;
    }

    /**
     * 返回所有的名称
     *
     * @return 所有名称的集合
     */
    @Override
    public Set<String> keySet() {
        return this.nameValuePairs.keySet();
    }

    /**
     * 返回所有的值
     *
     * @return 所有值的集合
     */
    @Override
    public Collection<Object> values() {
        return this.nameValuePairs.values();
    }

    /**
     * 返回所有名称-映射
     *
     * @return 所有名称-映射形式的集合
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.nameValuePairs.entrySet();
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     * （如果{@code name}或{@code value}存在null值，则什么也不做）
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    @NonNull
    public JsonObject putOpt(String name, Object value) {
        if (name == null || value == null) {
            return this;
        }
        return put(name, value);
    }

    /**
     * 将value映射到name的数组中，如果此对象没有name映射，则会插入一个新映射。
     * 如果映射存在但其值不是数组，则将现有值和新值按顺序插入到本身映射到name的新数组中
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    public JsonObject accumulate(String name, Object value) {
        Object current = this.nameValuePairs.get(checkName(name));
        if (current == null) {
            return put(name, value);
        }

        if (value instanceof Number) {
            Json.checkDouble(((Number) value).doubleValue());
        }

        if (current instanceof JsonArray) {
            JsonArray array = (JsonArray) current;
            array.put(value);
        } else {
            JsonArray array = new JsonArray();
            array.put(current);
            array.put(value);
            this.nameValuePairs.put(name, array);
        }
        return this;
    }

    String checkName(String name) {
        if (name == null) {
            throw new JsonException("Names must be non-null");
        }
        return name;
    }

    /**
     * 如果存在，则删除对应映射
     *
     * @param name - 属性的名称
     * @return 先前由name映射的值，如果没有这样的映射，则为 null
     */
    public Object remove(String name) {
        return this.nameValuePairs.remove(name);
    }

    /**
     * 如果没有此name映射或者它具有值为NULL的映射，则返回 true
     *
     * @param name - 属性的名称
     * @return 如果此对象没有name映射，则为 true
     */
    public boolean isNull(String name) {
        Object value = this.nameValuePairs.get(name);
        return value == null || value == NULL;
    }

    /**
     * 如果此对象具有name的映射，则返回 true。 映射可能是NULL
     *
     * @param name - 属性的名称
     * @return 如果此对象具有name映射，则为 true
     */
    public boolean has(String name) {
        return this.nameValuePairs.containsKey(name);
    }

    /**
     * 返回name映射的值，
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在则抛出异常{@link JsonException}
     */
    public Object get(String name) {
        Object result = this.nameValuePairs.get(name);
        if (result == null) {
            throw new JsonException("No value for " + name);
        }
        return result;
    }

    /**
     * 返回name映射的值，如果不存在这样的映射，则返回 null
     *
     * @param name - 属性的名称
     * @return 值或null
     */
    public Object opt(String name) {
        return this.nameValuePairs.get(name);
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Boolean} 或可以强制为 {@link Boolean} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Boolean getBoolean(String name) {
        Object object = get(name);
        Boolean result = Json.toBoolean(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "boolean");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BooleanConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param booleanConvert - Boolean转换器
     */
    public Boolean getBoolean(String name, @NonNull BooleanConvert booleanConvert) {
        Object object = get(name);
        return booleanConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BooleanConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param booleanConvert - Boolean转换器
     * @param defaultValue  - 默认值
     */
    public Boolean getBoolean(String name, @NonNull BooleanConvert booleanConvert, Boolean defaultValue) {
        Object object = get(name);
        return booleanConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是布尔值或可以强制为布尔值），否则返回 false
     *
     * @param name - 属性的名称
     * @return 值或 false
     */
    public boolean optBoolean(String name) {
        return optBoolean(name, false);
    }

    /**
     * 返回name映射的值，（如果它存在并且是布尔值或可以强制为布尔值），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public boolean optBoolean(String name, boolean fallback) {
        Object object = opt(name);
        Boolean result = Json.toBoolean(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Byte} 或可以强制为 {@link Byte} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Byte getByte(String name) {
        Object object = get(name);
        Byte result = Json.toByte(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "byte");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link ByteConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param byteConvert - Byte转换器
     */
    public Byte getByte(String name, @NonNull ByteConvert byteConvert) {
        Object object = get(name);
        return byteConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link ByteConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param byteConvert - Byte转换器
     * @param defaultValue - 默认值
     */
    public Byte getByte(String name, @NonNull ByteConvert byteConvert, Byte defaultValue) {
        Object object = get(name);
        return byteConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是byte值或可以强制为byte值），否则返回 0
     *
     * @param name - 属性的名称
     * @return 值或 0
     */
    public byte optByte(String name) {
        return optByte(name, (byte) 0);
    }

    /**
     * 返回name映射的值，（如果它存在并且是byte值或可以强制为byte值），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public byte optByte(String name, byte fallback) {
        Object object = opt(name);
        Byte result = Json.toByte(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Short} 或可以强制为 {@link Short} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Short getShort(String name) {
        Object object = get(name);
        Short result = Json.toShort(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "short");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link ShortConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param shortConvert - Short转换器
     */
    public Short getShort(String name, @NonNull ShortConvert shortConvert) {
        Object object = get(name);
        return shortConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link ShortConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param shortConvert - Short转换器
     * @param defaultValue - 默认值
     */
    public Short getShort(String name, @NonNull ShortConvert shortConvert, Short defaultValue) {
        Object object = get(name);
        return shortConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是short值或可以强制为short值），否则返回 0
     *
     * @param name - 属性的名称
     * @return 值或 0
     */
    public short optShort(String name) {
        return optShort(name, (short) 0);
    }

    /**
     * 返回name映射的值，（如果它存在并且是short值或可以强制为short值），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public Short optShort(String name, short fallback) {
        Object object = opt(name);
        Short result = Json.toShort(object);
        return result != null ? result : fallback;
    }


    /**
     * 返回name映射的值，（如果它存在并且是 {@link Integer} 或可以强制为 {@link Integer} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Integer getInteger(String name) {
        Object object = get(name);
        Integer result = Json.toInteger(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "int");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link IntegerConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param integerConvert - Integer转换器
     */
    public Integer getInteger(String name, @NonNull IntegerConvert integerConvert) {
        Object object = get(name);
        return integerConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link IntegerConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param integerConvert - Integer转换器
     * @param defaultValue - 默认值
     */
    public Integer getInteger(String name, @NonNull IntegerConvert integerConvert, Integer defaultValue) {
        Object object = get(name);
        return integerConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 int 或可以强制为一个 int ），否则返回 0
     *
     * @param name - 属性的名称
     * @return 值或 0
     */
    public int optInt(String name) {
        return optInt(name, 0);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 int 或可以强制为一个 int ），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public int optInt(String name, int fallback) {
        Object object = opt(name);
        Integer result = Json.toInteger(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Long} 或可以强制为 {@link Long} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Long getLong(String name) {
        Object object = get(name);
        Long result = Json.toLong(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "long");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link LongConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param longConvert - Long转换器
     */
    public Long getLong(String name, @NonNull LongConvert longConvert) {
        Object object = get(name);
        return longConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link LongConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param longConvert - Long转换器
     * @param defaultValue - 默认值
     */
    public Long getLong(String name, @NonNull LongConvert longConvert, Long defaultValue) {
        Object object = get(name);
        return longConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 long 或可以强制为一个 long ），否则返回 0
     *
     * @param name - 属性的名称
     * @return 值或 0
     */
    public long optLong(String name) {
        return optLong(name, 0L);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 long 或可以强制为一个 long ），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public long optLong(String name, long fallback) {
        Object object = opt(name);
        Long result = Json.toLong(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Float} 或可以强制为 {@link Float} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Float getFloat(String name) {
        Object object = get(name);
        Float result = Json.toFloat(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "float");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link FloatConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param floatConvert - Float转换器
     */
    public Float getFloat(String name, @NonNull FloatConvert floatConvert) {
        Object object = get(name);
        return floatConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link FloatConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param floatConvert - Float转换器
     * @param defaultValue - 默认值
     */
    public Float getFloat(String name, @NonNull FloatConvert floatConvert, Float defaultValue) {
        Object object = get(name);
        return floatConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 float 或可以强制为一个 float ），否则返回 0
     *
     * @param name - 属性的名称
     * @return 值或 0
     */
    public float optFloat(String name) {
        return optFloat(name, 0F);
    }

    /**
     * 返回name映射的值，（如果它存在并且是一个 float 或可以强制为一个 float ），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public float optFloat(String name, float fallback) {
        Object object = opt(name);
        Float result = Json.toFloat(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Double} 或可以强制为 {@link Double} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Double getDouble(String name) {
        Object object = get(name);
        Double result = Json.toDouble(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "double");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DoubleConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param doubleConvert - Double转换器
     */
    public Double getDouble(String name, @NonNull DoubleConvert doubleConvert) {
        Object object = get(name);
        return doubleConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DoubleConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param doubleConvert - Double转换器
     * @param defaultValue - 默认值
     */
    public Double getDouble(String name, @NonNull DoubleConvert doubleConvert, Double defaultValue) {
        Object object = get(name);
        return doubleConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是双精度值或可以强制为双精度值），否则返回 NaN
     *
     * @param name - 属性的名称
     * @return 值或 NaN
     */
    public double optDouble(String name) {
        return optDouble(name, Double.NaN);
    }

    /**
     * 返回name映射的值，（如果它存在并且是双精度值或可以强制为双精度值），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public double optDouble(String name, double fallback) {
        Object object = opt(name);
        Double result = Json.toDouble(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Character} 或可以强制为 {@link Character} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Character getCharacter(String name) {
        Object object = get(name);
        Character result = Json.toCharacter(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "char");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link CharacterConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param characterConvert - Character转换器
     */
    public Character getCharacter(String name, @NonNull CharacterConvert characterConvert) {
        Object object = get(name);
        return characterConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link CharacterConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param characterConvert - Character转换器
     * @param defaultValue - 默认值
     */
    public Character getCharacter(String name, @NonNull CharacterConvert characterConvert, Character defaultValue) {
        Object object = get(name);
        return characterConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是char值或可以强制为char值），否则返回 '\u0000'（即空格）
     *
     * @param name - 属性的名称
     * @return 值或 NaN
     */
    public char optCharacter(String name) {
        return optCharacter(name, Character.MIN_VALUE);
    }

    /**
     * 返回name映射的值，（如果它存在并且是char值或可以强制为char值），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public char optCharacter(String name, char fallback) {
        Object object = opt(name);
        Character result = Json.toCharacter(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在 ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public String getString(String name) {
        Object object = get(name);
        String result = Json.toString(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "String");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link StringConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param stringConvert - String转换器
     */
    public String getString(String name, @NonNull StringConvert stringConvert) {
        Object object = get(name);
        return stringConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link StringConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param stringConvert - String转换器
     * @param defaultValue - 默认值
     */
    public String getString(String name, @NonNull StringConvert stringConvert, String defaultValue) {
        Object object = get(name);
        return stringConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在），否则返回空字符串
     *
     * @param name - 属性的名称
     * @return 值或空字符串
     */
    public String optString(String name) {
        return optString(name, "");
    }

    /**
     * 返回name映射的值，（如果它存在），否则返回 {@code fallback}
     *
     * @param name     - 属性的名称
     * @param fallback - 备选值
     * @return 对应的值或 {@code fallback}
     */
    public String optString(String name, String fallback) {
        Object object = opt(name);
        String result = Json.toString(object);
        return result != null ? result : fallback;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link BigDecimal} 或可以强制为 {@link BigDecimal} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public BigDecimal getBigDecimal(String name) {
        Object object = get(name);
        BigDecimal result = Json.toBigDecimal(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "BigDecimal");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BigDecimalConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param bigDecimalConvert - BigDecimal转换器
     */
    public BigDecimal getBigDecimal(String name, @NonNull BigDecimalConvert bigDecimalConvert) {
        Object object = get(name);
        return bigDecimalConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BigDecimalConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param bigDecimalConvert - BigDecimal转换器
     * @param defaultValue - 默认值
     */
    public BigDecimal getBigDecimal(String name, @NonNull BigDecimalConvert bigDecimalConvert, BigDecimal defaultValue) {
        Object object = get(name);
        return bigDecimalConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link BigInteger} 或可以强制为 {@link BigInteger} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public BigInteger getBigInteger(String name) {
        Object object = get(name);
        BigInteger result = Json.toBigInteger(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "BigInteger");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BigIntegerConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param bigIntegerConvert - BigInteger转换器
     */
    public BigInteger getBigInteger(String name, @NonNull BigIntegerConvert bigIntegerConvert) {
        Object object = get(name);
        return bigIntegerConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link BigIntegerConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param bigIntegerConvert - BigInteger转换器
     * @param defaultValue - 默认值
     */
    public BigInteger getBigInteger(String name, @NonNull BigIntegerConvert bigIntegerConvert, BigInteger defaultValue) {
        Object object = get(name);
        return bigIntegerConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Date} 或可以强制为 {@link Date} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Date getDate(String name) {
        Object object = get(name);
        Date result = Json.toDate(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "Date");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param dateConvert - Date解析器
     */
    public Date getDate(String name, @NonNull DateConvert dateConvert) {
        Object object = get(name);
        return dateConvert.convert(object);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param dateConvert - Date解析器
     * @param defaultValue - 默认值
     */
    public Date getDate(String name, @NonNull DateConvert dateConvert, Date defaultValue) {
        Object object = get(name);
        return dateConvert.convert(object, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link Date} 或可以强制为 {@link Date} ）
     *
     * @param name - 属性的名称
     * @param format - 解析格式
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Date getDate(String name, String format) {
        Object object = get(name);
        Date result = Json.toDate(object, format);
        if (result == null) {
            throw Json.typeMismatch(name, object, "Date");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）
     *
     * @param name - 属性的名称
     * @param format - 解析格式
     * @param dateConvert - Date解析器
     */
    public Date getDate(String name, String format, @NonNull DateConvert dateConvert) {
        Object object = get(name);
        return dateConvert.convert(object, format);
    }

    /**
     * 返回name映射的值，（使用自定义转换器 {@link DateConvert} 进行值的转换 ）， 并定义返回默认值的触发条件
     *
     * @param name - 属性的名称
     * @param format - 解析格式
     * @param dateConvert - Date解析器
     * @param defaultValue - 默认值
     */
    public Date getDate(String name, String format, @NonNull DateConvert dateConvert, Date defaultValue) {
        Object object = get(name);
        return dateConvert.convert(object, format, defaultValue);
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link JsonArray} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public JsonArray getJsonArray(String name) {
        Object object = get(name);
        if (object instanceof JsonArray) {
            return (JsonArray) object;
        }  else if (object instanceof Collection) {
            return new JsonArray(((Collection<?>) object));
        } else if (object.getClass().isArray()) {
            return new JsonArray(object);
        } else {
            throw Json.typeMismatch(name, object, "JSONArray");
        }
    }

    /**
     * 返回name映射的值，（如果它存在），否则返回 null
     *
     * @param name - 属性的名称
     * @return 值或 null
     */
    public JsonArray optJsonArray(String name) {
        Object object = opt(name);
        return object instanceof JsonArray ? (JsonArray) object : null;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link List<JsonObject>} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public List<JsonObject> getJsonObjects(String name) {
        Object array = get(name);
        if (!array.getClass().isArray()) {
            throw new JsonException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        List<JsonObject> jsonObjects = new ArrayList<>(length);
        for (int i = 0; i < length; ++i) {
            Object o = Array.get(array, i);
            if (o instanceof JsonObject) {
                jsonObjects.add((JsonObject) o);
            } else {
                throw Json.typeMismatch(name, o, "JSONObject");
            }
        }
        return jsonObjects;
    }

    /**
     * 返回name映射的值，（如果它存在），否则返回 empty
     *
     * @param name - 属性的名称
     * @return 值或 empty
     */
    public List<JsonObject> optJsonObjects(String name) {
        Object array = get(name);
        if (!array.getClass().isArray()) {
            return Collections.emptyList();
        }
        final int length = Array.getLength(array);
        List<JsonObject> jsonObjects = new ArrayList<>(length);
        for (int i = 0; i < length; ++i) {
            Object o = Array.get(array, i);
            if (o instanceof JsonObject) {
                jsonObjects.add((JsonObject) o);
            }
        }
        return jsonObjects;
    }

    /**
     * 返回name映射的值，（如果它存在并且是 {@link JsonObject} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public JsonObject getJsonObject(String name) {
        Object object = get(name);
        if (object instanceof JsonObject) {
            return (JsonObject) object;
        } else {
            throw Json.typeMismatch(name, object, "JSONObject");
        }
    }

    /**
     * 返回name映射的值，（如果它存在），否则返回 null
     *
     * @param name - 属性的名称
     * @return 值或 null
     */
    public JsonObject optJsonObject(String name) {
        Object object = opt(name);
        return object instanceof JsonObject ? (JsonObject) object : null;
    }

    /**
     * 返回一个数组，其值对应于 names
     * 对于未映射的名称，该数组包含 null
     * 如果 names 为 null 或为空，则此方法返回 null
     *
     * @param names - 属性的名称
     */
    public JsonArray toJsonArray(JsonArray names) {
        JsonArray result = new JsonArray();
        if (names == null) {
            return null;
        }
        int length = names.length();
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            String name = Json.toString(names.opt(i));
            result.put(opt(name));
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用实现的转换器）
     * @param name - 属性的名称
     * @param convertClass - 转换器
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public <T> T getValue(String name, @NonNull ConvertClass<T> convertClass) {
        Object object = get(name);
        T result = convertClass.convert(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "T");
        }
        return result;
    }

    /**
     * 返回name映射的值，（使用实现的转换器），并定义返回默认值的触发条件
     * @param name - 属性的名称
     * @param convertClass - 转换器
     * @param defaultValue - 默认值
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public <T> T getValue(String name, @NonNull ConvertClass<T> convertClass, T defaultValue) {
        Object object = get(name);
        T result = convertClass.convert(object, defaultValue);
        if (result == null) {
            throw Json.typeMismatch(name, object, "T");
        }
        return result;
    }

    /**
     * 转换为指定Bean
     *
     * @param tClass - Bean类型
     * @return 包含属性的Bean对象
     */
    public <T> T toClass(@NonNull Class<T> tClass) {
        try {
            T instance = tClass.newInstance();
            BeanMap beanMap = BeanMap.create(instance);
            beanMap.putAll(this.nameValuePairs);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JsonException(e.getMessage(), e);
        }
    }

    /**
     * 根据自定义转换器转换为指定类型Bean
     *
     * @param convertClass - 自定义转换器
     * @return 指定类型Bean对象
     */
    public <T> T toClass(@NonNull ConvertClass<T> convertClass) {
        return convertClass.convert(this.nameValuePairs);
    }

    /**
     * 根据自定义转换器转换为指定类型Bean，并定义返回默认值的触发条件
     *
     * @param convertClass - 自定义转换器
     * @param defaultValue - 默认值
     * @return 指定类型Bean对象
     */
    public <T> T toClass(@NonNull ConvertClass<T> convertClass, T defaultValue) {
        return convertClass.convert(this.nameValuePairs, defaultValue);
    }

    /**
     * 返回此对象中String名称的迭代器
     */
    @SuppressWarnings("rawtypes")
    public Iterator keys() {
        return this.nameValuePairs.keySet().iterator();
    }

    /**
     * 返回一个包含此对象中String名称的数组
     */
    public JsonArray names() {
        return this.nameValuePairs.isEmpty() ? null : new JsonArray(new ArrayList<>(this.nameValuePairs.keySet()));
    }

    /**
     * 将对象内的Date值进行格式化
     *
     * @return 格式化后的对象
     */
    public JsonObject dateFormat() {
        Set<Entry<String, Object>> entries = this.nameValuePairs.entrySet();
        for (Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            Date date;
            if (value instanceof Date) {
                date = (Date) value;
            } else if (value instanceof Calendar) {
                date = ((Calendar) value).getTime();
            } else {
                continue;
            }
            String format = Objects.isNull(EasyProperties.getDateFormat()) ? "yyyy-MM-dd HH:mm:ss" : EasyProperties.getDateFormat();
            String s = new SimpleDateFormat(format).format(date);
            putOpt(entry.getKey(), s);
        }
        return this;
    }

    /**
     * 将对象内的键转换为小写
     *
     * @return 格式化后的对象
     */
    public JsonObject toLower() {
        Set<Entry<String, Object>> entries = this.nameValuePairs.entrySet();
        for (Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            renameKey(key, key.toLowerCase());
        }
        return this;
    }

    @Override
    public String toString() {
        try {
            JsonStringer stringer = new JsonStringer();
            writeTo(stringer);
            return stringer.toString();
        } catch (JsonException e) {
            return "";
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
        stringer.object();
        for (Map.Entry<String, Object> entry : this.nameValuePairs.entrySet()) {
            stringer.key(entry.getKey()).value(entry.getValue());
        }
        stringer.endObject();
    }




}
