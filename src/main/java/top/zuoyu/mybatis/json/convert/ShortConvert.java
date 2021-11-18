package top.zuoyu.mybatis.json.convert;

/**
 * Short类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:44
 */
public interface ShortConvert extends ConvertClass<Short> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Short}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Short convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Short}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Short convert(Object value, Short defaultValue);
}
