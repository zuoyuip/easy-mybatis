package top.zuoyu.mybatis.data.enums;

/**
 * 元信息内表类型 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 14:09
 */
public enum TableType {

    /**
     * 表
     */
    TABLE("TABLE"),

    /**
     * 视图
     */
    VIEW("VIEW"),
    SYSTEM_TABLE ("SYSTEM TABLE"),
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    ALIAS("ALIAS"),
    SYNONYM("SYNONYM");

    private final String value;

    /**
     * 构造
     * @param value 值
     */
    TableType(String value){
        this.value = value;
    }
    /**
     * 获取值
     * @return 值
     */
    public String value(){
        return this.value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
