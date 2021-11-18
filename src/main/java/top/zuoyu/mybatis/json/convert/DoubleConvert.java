package top.zuoyu.mybatis.json.convert;

/**
 * Double类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:50
 */
public interface DoubleConvert extends ConvertClass<Double> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Double}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Double convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Double}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Double convert(Object value, Double defaultValue);
}
