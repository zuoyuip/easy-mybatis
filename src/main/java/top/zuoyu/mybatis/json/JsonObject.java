/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * {@author: zuoyu }在其基础上进行了改动
 */

package top.zuoyu.mybatis.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
public class JsonObject {

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
    private final Map<String, Object> nameValuePairs;

    public JsonObject() {
        this.nameValuePairs = new LinkedHashMap<>();
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
    public JsonObject(@NonNull JsonTokener readFrom) throws JsonException {
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
    public JsonObject(String json) throws JsonException {
        this(new JsonTokener(json));
    }

    /**
     * 通过从给定对象复制列出的名称的映射，创建一个新的JsonObject，
     * copyFrom中不存在的copyFrom将被跳过
     *
     * @param copyFrom – 来源
     * @param names    - 属性名称
     */
    public JsonObject(@NonNull JsonObject copyFrom, @NonNull String[] names) throws JsonException {
        this();
        for (String name : names) {
            Object value = copyFrom.opt(name);
            if (value != null) {
                this.nameValuePairs.put(name, value);
            }
        }
    }

    /**
     * 将数字转化为 JSON 字符串
     *
     * @param number - 数字
     */
    public static String numberToString(Number number) throws JsonException {
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
    public static Object wrap(@NonNull Object o) {
        if (o == null) {
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
     * 返回此对象中名称/值映射的数量
     */
    public int length() {
        return this.nameValuePairs.size();
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    public JsonObject put(String name, boolean value) throws JsonException {
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
    public JsonObject put(String name, double value) throws JsonException {
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
    public JsonObject put(String name, int value) throws JsonException {
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
    public JsonObject put(String name, long value) throws JsonException {
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
    public JsonObject put(String name, Object value) throws JsonException {
        if (value == null) {
            this.nameValuePairs.remove(name);
            return this;
        }
        if (value instanceof Number) {
            Json.checkDouble(((Number) value).doubleValue());
        }
        this.nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * 将value映射到name ，破坏任何具有相同名称的现有名称/值映射
     * （如果{@code name}或{@code value}存在null值，则什么也不做）
     *
     * @param name  - 属性的名称
     * @param value – 对应的值
     * @return {@link JsonObject}
     */
    public JsonObject putOpt(String name, Object value) throws JsonException {
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
    public JsonObject accumulate(String name, Object value) throws JsonException {
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

    String checkName(String name) throws JsonException {
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
    public Object get(String name) throws JsonException {
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
    public Boolean getBoolean(String name) throws JsonException {
        Object object = get(name);
        Boolean result = Json.toBoolean(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "boolean");
        }
        return result;
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
     * 返回name映射的值，（如果它存在并且是 {@link Double} 或可以强制为 {@link Double} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Double getDouble(String name) throws JsonException {
        Object object = get(name);
        Double result = Json.toDouble(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "double");
        }
        return result;
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
     * 返回name映射的值，（如果它存在并且是 {@link Integer} 或可以强制为 {@link Integer} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public Integer getInteger(String name) throws JsonException {
        Object object = get(name);
        Integer result = Json.toInteger(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "int");
        }
        return result;
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
    public Long getLong(String name) throws JsonException {
        Object object = get(name);
        Long result = Json.toLong(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "long");
        }
        return result;
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
     * 返回name映射的值，（如果它存在 ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public String getString(String name) throws JsonException {
        Object object = get(name);
        String result = Json.toString(object);
        if (result == null) {
            throw Json.typeMismatch(name, object, "String");
        }
        return result;
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
     * 返回name映射的值，（如果它存在并且是 {@link JsonArray} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public JsonArray getJsonArray(String name) throws JsonException {
        Object object = get(name);
        if (object instanceof JsonArray) {
            return (JsonArray) object;
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
     * 返回name映射的值，（如果它存在并且是 {@link JsonObject} ）
     *
     * @param name - 属性的名称
     * @throws JsonException 如果不存在或无法强制转换则抛出异常{@link JsonException}
     */
    public JsonObject getJsonObject(String name) throws JsonException {
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
        stringer.object();
        for (Map.Entry<String, Object> entry : this.nameValuePairs.entrySet()) {
            stringer.key(entry.getKey()).value(entry.getValue());
        }
        stringer.endObject();
    }

}
