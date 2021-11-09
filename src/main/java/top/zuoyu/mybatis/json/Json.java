
package top.zuoyu.mybatis.json;

import top.zuoyu.mybatis.exception.JsonException;

/**
 * 转换工具 .
 *
 * @author: zuoyu
 * @create: 2021-11-04 10:00
 */
class Json {

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
			}
			catch (NumberFormatException ignored) {
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
			}
			catch (NumberFormatException ignored) {
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
			}
			catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	static String toString(Object value) {
		if (value instanceof String) {
			return (String) value;
		}
		if (value != null) {
			return String.valueOf(value);
		}
		return null;
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
