package top.zuoyu.mybatis.json.convert;

/**
 * Long类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:47
 */
public interface LongConvert extends ConvertClass<Long> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Long}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Long convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Long}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Long convert(Object value, Long defaultValue);
}
