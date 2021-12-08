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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.exception.JsonException;
import top.zuoyu.mybatis.utils.DateUtils;

/**
 * 转换工具 .
 *
 * @author: zuoyu
 * @create: 2021-11-04 10:00
 */
class Json {

    private static final int BIG_LENGTH = 65535;

    private static final int BIG_DECIMAL_SCALE = 100;
    private static final int BIG_INTEGER_SCALE = 1000;


    static double checkDouble(double d) throws JsonException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new JsonException("Forbidden numeric value: " + d);
        }
        return d;
    }

    static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            String stringValue = (String) value;
            if (Boolean.TRUE.toString().equalsIgnoreCase(stringValue)) {
                return true;
            }
            if (Boolean.FALSE.toString().equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    static Byte toByte(Object value) {
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        if (value instanceof String) {
            try {
                return (byte) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Short toShort(Object value) {
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        if (value instanceof String) {
            try {
                return (short) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Float toFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            try {
                return (float) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }


    static Character toCharacter(Object value) {
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() < 1) {
                throw new JsonException("can not cast to char, value : " + value);
            }
            return strVal.charAt(0);
        }
        return null;
    }

    static String toString(@NonNull Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    static BigDecimal toBigDecimal(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof Float) {
            if (Float.isNaN((Float) value) || Float.isInfinite((Float) value)) {
                return null;
            }
        } else if (value instanceof Double) {
            if (Double.isNaN((Double) value) || Double.isInfinite((Double) value)) {
                return null;
            }
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }

        String strVal = value.toString();

        if (strVal.length() == 0
                || "null".equalsIgnoreCase(strVal)) {
            return null;
        }

        if (strVal.length() > BIG_LENGTH) {
            throw new JsonException("decimal overflow");
        }
        return new BigDecimal(strVal);
    }

    static BigInteger toBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            if (Float.isNaN((Float) value) || Float.isInfinite((Float) value)) {
                return null;
            }
            return BigInteger.valueOf(((Float) value).longValue());
        } else if (value instanceof Double) {
            if (Double.isNaN((Double) value) || Double.isInfinite((Double) value)) {
                return null;
            }
            return BigInteger.valueOf(((Double) value).longValue());
        } else if (value instanceof BigInteger) {
            return (BigInteger) value;
        } else if (value instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) value;
            int scale = decimal.scale();
            if (scale > -BIG_INTEGER_SCALE && scale < BIG_INTEGER_SCALE) {
                return ((BigDecimal) value).toBigInteger();
            }
        }

        String strVal = value.toString();

        if (strVal.length() == 0
                || "null".equalsIgnoreCase(strVal)) {
            return null;
        }

        if (strVal.length() > BIG_LENGTH) {
            throw new JsonException("decimal overflow");
        }
        return new BigInteger(strVal);
    }

    static Date toDate(Object value) {
        return toDate(value, null);
    }

    static Date toDate(Object value, String format) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence && StringUtils.hasLength(value.toString())) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        long longValue = -1;

        if (value instanceof BigDecimal) {
            BigDecimal bigDecimal = ((BigDecimal) value);
            int scale = bigDecimal.scale();
            if (scale >= -BIG_DECIMAL_SCALE && scale <= BIG_DECIMAL_SCALE) {
                return new Date(longValue);
            }
            return new Date(bigDecimal.longValueExact());
        }

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
            return new Date(longValue);
        }

        if (value instanceof TemporalAccessor) {
            LocalDateTime localDateTime = LocalDateTime.from((TemporalAccessor) value);
            return DateUtils.asDate(localDateTime);
        }

        if (value instanceof CharSequence) {
            if (!StringUtils.hasLength(format)) {
                LocalDateTime localDateTime = LocalDateTime.parse(value.toString());
                return DateUtils.asDate(localDateTime);
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), dateTimeFormatter);
            return DateUtils.asDate(localDateTime);
        }
        throw typeMismatch(value, "date");
    }


    public static JsonException typeMismatch(Object indexOrName, Object actual, String requiredType)
            throws JsonException {
        if (actual == null) {
            throw new JsonException("Value at " + indexOrName + " is null.");
        }
        throw new JsonException("Value " + actual + " at " + indexOrName + " of type " + actual.getClass().getName()
                + " cannot be converted to " + requiredType);
    }

    public static JsonException typeMismatch(Object actual, String requiredType) throws JsonException {
        if (actual == null) {
            throw new JsonException("Value is null.");
        }
        throw new JsonException("Value " + actual + " of type " + actual.getClass().getName()
                + " cannot be converted to " + requiredType);
    }

}
