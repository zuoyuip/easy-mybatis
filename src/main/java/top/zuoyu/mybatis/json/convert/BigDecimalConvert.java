package top.zuoyu.mybatis.json.convert;

import java.math.BigDecimal;

/**
 * BigDecimal类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 15:00
 */
public interface BigDecimalConvert extends ConvertClass<BigDecimal> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link BigDecimal}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    BigDecimal convert(Object value);

    /**
     * 将给定的 {@code value} 对象转换为 {@link BigDecimal}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    BigDecimal convert(Object value, BigDecimal defaultValue);
}
