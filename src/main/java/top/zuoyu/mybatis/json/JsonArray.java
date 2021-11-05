/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * {@author: zuoyu }在其基础上进行了改动
 */

package top.zuoyu.mybatis.json;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.exception.JsonException;

/**
 * Json数组 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 10:00
 */
public class JsonArray {

    private final List<Object> values;

    public JsonArray() {
        this.values = new ArrayList<>();
    }

    /**
     * 通过复制给定集合中的所有值来创建一个新的JsonArray
     *
     * @param copyFrom – 一个集合
     */
    @SuppressWarnings("rawtypes")
    public JsonArray(Collection copyFrom) {
        this();
        if (Objects.nonNull(copyFrom)) {
            for (Object o : copyFrom) {
                put(JsonObject.wrap(o));
            }
        }
    }

    /**
     * 使用标记器中下一个数组的值创建一个新的JsonArray
     *
     * @param readFrom - 标记器
     */
    public JsonArray(@NonNull JsonTokener readFrom) throws JsonException {
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
    public JsonArray(String json) throws JsonException {
        this(new JsonTokener(json));
    }

    /**
     * 使用给定原始数组中的值创建一个新的JsonArray
     *
     * @param array - 给定数组
     */
    public JsonArray(@NonNull Object array) throws JsonException {
        if (!array.getClass().isArray()) {
            throw new JsonException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        this.values = new ArrayList<>(length);
        for (int i = 0; i < length; ++i) {
            put(JsonObject.wrap(Array.get(array, i)));
        }
    }

    /**
     * 使用给定原始数组中的值创建一个新的JsonArray
     *
     * @param array - 给定数组
     */
    public JsonArray(@NonNull Object[] array) throws JsonException {
        final int length = Array.getLength(array);
        this.values = new ArrayList<>(length);
        for (int i = 0; i < length; ++i) {
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
    public JsonArray put(double value) throws JsonException {
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
    public JsonArray put(int index, boolean value) throws JsonException {
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
    public JsonArray put(int index, double value) throws JsonException {
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
    public JsonArray put(int index, int value) throws JsonException {
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
    public JsonArray put(int index, long value) throws JsonException {
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
    public JsonArray put(int index, Object value) throws JsonException {
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
     * 返回索引处的值
     *
     * @param index - 索引
     * @return 对应的值
     */
    public Object get(int index) throws JsonException {
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

    /**
     * 返回索引处的值，如果数组在index处没有值，则返回 null
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
    public Object remove(int index) {
        if (index < 0 || index >= this.values.size()) {
            return null;
        }
        return this.values.remove(index);
    }

    /**
     * 返回索引处的值为 {@link Boolean}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Boolean getBoolean(int index) throws JsonException {
        Object object = get(index);
        Boolean result = Json.toBoolean(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "boolean");
        }
        return result;
    }

    /**
     * 返回索引处的值（如果它存在并且是布尔值或可以强制为布尔值）,否则返回 false
     *
     * @param index - 索引
     * @return 对应的值
     */
    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    /**
     * 返回索引处的值（如果它存在并且是布尔值或可以强制为布尔值）,否则返回 {@code fallback}
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
     * 返回索引处的值为 {@link Double}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Double getDouble(int index) throws JsonException {
        Object object = get(index);
        Double result = Json.toDouble(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "double");
        }
        return result;
    }

    /**
     * 返回索引处的值（如果它存在并且是双精度值或可以强制为双精度值）,否则返回 NaN
     *
     * @param index - 索引
     * @return 对应的值
     */
    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    /**
     * 返回索引处的值（如果它存在并且是双精度值或可以强制为双精度值）,否则返回 {@code fallback}
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
     * 返回索引处的值为 {@link Integer}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Integer getInteger(int index) throws JsonException {
        Object object = get(index);
        Integer result = Json.toInteger(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "int");
        }
        return result;
    }

    /**
     * 返回索引处的值（如果它存在并且是一个 int 或者可以被强制转换为一个 int）,否则返回 0
     *
     * @param index - 索引
     * @return 对应的值或 0
     */
    public int optInt(int index) {
        return optInt(index, 0);
    }

    /**
     * 返回索引处的值（如果它存在并且是一个 int 或者可以被强制转换为一个 int）,否则返回否则返回 {@code fallback}
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
     * 返回索引处的值为 {@link Long}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public Long getLong(int index) throws JsonException {
        Object object = get(index);
        Long result = Json.toLong(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "long");
        }
        return result;
    }

    /**
     * 返回索引处的值（如果它存在并且是一个 long 或者可以被强制转换为一个 long）,否则返回 0
     *
     * @param index - 索引
     * @return 对应的值或 0
     */
    public long optLong(int index) {
        return optLong(index, 0L);
    }

    /**
     * 返回索引处的值（如果它存在并且是一个 long 或者可以被强制转换为一个 long）,否则返回 {@code fallback}
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
     * 返回索引处的值为 {@link String}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果该值不存在
     */
    public String getString(int index) throws JsonException {
        Object object = get(index);
        String result = Json.toString(object);
        if (Objects.isNull(result)) {
            throw Json.typeMismatch(index, object, "String");
        }
        return result;
    }

    /**
     * 返回索引处的值（必要时对其进行强制转换。 如果不存在这样的值，则返回空字符串）
     *
     * @param index - 索引
     * @return 对应的值或空字符串
     */
    public String optString(int index) {
        return optString(index, "");
    }

    /**
     * 返回索引处的值（必要时对其进行强制转换。 如果不存在这样的值，则返回 {@code fallback}）
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
     * 返回索引处的值为 {@link JsonArray}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public JsonArray getJsonArray(int index) throws JsonException {
        Object object = get(index);
        if (object instanceof JsonArray) {
            return (JsonArray) object;
        } else {
            throw Json.typeMismatch(index, object, "JSONArray");
        }
    }

    /**
     * 返回索引处的值（如果它存在并且是JSONArray）,否则返回 null
     *
     * @param index - 索引
     * @return 对应的值
     */
    public JsonArray optJsonArray(int index) {
        Object object = opt(index);
        return object instanceof JsonArray ? (JsonArray) object : null;
    }

    /**
     * 返回索引处的值为 {@link JsonObject}
     *
     * @param index - 索引
     * @return 对应的值
     * @throws JsonException - 如果index处的值不存在或无法强制转换则抛出异常
     */
    public JsonObject getJsonObject(int index) throws JsonException {
        Object object = get(index);
        if (object instanceof JsonObject) {
            return (JsonObject) object;
        } else {
            throw Json.typeMismatch(index, object, "JSONObject");
        }
    }

    /**
     * 返回索引处的值（如果它存在并且是JsonObject）,否则返回 null
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
    public JsonObject toJsonObject(@NonNull JsonArray names) throws JsonException {
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
    public List<JsonObject> toJsonObjects() throws JsonException {
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
            if (value instanceof JsonTokener) {
                jsonObjects.add(new JsonObject((JsonTokener) value));
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
    public String join(String separator) throws JsonException {
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
    public String toString(int indentSpaces) throws JsonException {
        JsonStringer stringer = new JsonStringer(indentSpaces);
        writeTo(stringer);
        return stringer.toString();
    }

    void writeTo(@NonNull JsonStringer stringer) throws JsonException {
        stringer.array();
        for (Object value : this.values) {
            stringer.value(value);
        }
        stringer.endArray();
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
