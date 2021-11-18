package top.zuoyu.mybatis.json.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigInteger类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 15:02
 */
public interface BigIntegerConvert extends ConvertClass<BigInteger> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link BigInteger}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    BigInteger convert(Object value);


    /**
     * 将给定的 {@code value} 对象转换为 {@link BigInteger}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    BigInteger convert(Object value, BigInteger defaultValue);
}
