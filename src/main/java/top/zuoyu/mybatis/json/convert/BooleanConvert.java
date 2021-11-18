package top.zuoyu.mybatis.json.convert;

/**
 * Boolean类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:36
 */
public interface BooleanConvert extends ConvertClass<Boolean> {


    /**
     * 将给定的 {@code value} 对象转换为 {@link Boolean}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Boolean convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Boolean}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Boolean convert(Object value, Boolean defaultValue);

}
