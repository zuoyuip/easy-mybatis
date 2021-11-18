package top.zuoyu.mybatis.json.convert;

/**
 * String类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:56
 */
public interface StringConvert extends ConvertClass<String> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link String}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    String convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link String}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    String convert(Object value, String defaultValue);
}
