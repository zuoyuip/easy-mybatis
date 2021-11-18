package top.zuoyu.mybatis.json.convert;

/**
 * Byte类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 14:42
 */
public interface ByteConvert extends ConvertClass<Byte> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Byte}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Byte convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Byte}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Byte convert(Object value, Byte defaultValue);
}
