package top.zuoyu.mybatis.json.convert;

/**
 * Float类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:48
 */
public interface FloatConvert extends ConvertClass<Float> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Float}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Float convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Float}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Float convert(Object value, Float defaultValue);
}
