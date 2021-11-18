package top.zuoyu.mybatis.json.convert;

import java.util.Date;

/**
 * Date类型转换器 .
 *
 * @author: zuoyu
 * @create: 2021-11-18 15:03
 */
public interface DateConvert extends ConvertClass<Date> {

    /**
     * 将给定的 {@code value} 对象转换为 {@link Date}
     *
     * @param value - 给定对象
     * @return 目标转换类型的对象
     */
    @Override
    Date convert(Object value);


    /**
     * 将给定的 {@code value} 对象转换为 {@link Date}
     *
     * @param value  - 给定对象
     * @param format - 给定的解析格式
     * @return 目标转换类型的对象
     */
    Date convert(Object value, String format);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Date}， 否则返回 {@code defaultValue}
     *
     * @param value        - 给定对象
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    @Override
    Date convert(Object value, Date defaultValue);

    /**
     * 将给定的 {@code value} 对象转换为 {@link Date}
     *
     * @param value        - 给定对象
     * @param format       - 给定的解析格式
     * @param defaultValue - 默认值
     * @return 目标转换类型的对象
     */
    Date convert(Object value, String format, Date defaultValue);
}
