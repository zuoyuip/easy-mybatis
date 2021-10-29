package top.zuoyu.mybatis.data.enums;

/**
 * 数据库表列元数据 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 10:58
 */
public enum ColumnMeta {

    /**
     * 表类别
     */
    TABLE_CAT("TABLE_CAT"),

    /**
     * 表模式,在oracle中获取的是命名空间
     */
    TABLE_SCHEM("TABLE_SCHEM"),

    /**
     * 表名
     */
    TABLE_NAME("TABLE_NAME"),

    /**
     * 列名
     */
    COLUMN_NAME("COLUMN_NAME"),

    /**
     * 对应的java.sql.Types的SQL类型(列类型ID)
     */
    DATA_TYPE("DATA_TYPE"),

    /**
     * java.sql.Types类型名称(列类型名称)
     */
    TYPE_NAME("TYPE_NAME"),

    /**
     * 列大小
     */
    COLUMN_SIZE("COLUMN_SIZE"),

    /**
     * 缓冲长度
     */
    BUFFER_LENGTH("BUFFER_LENGTH"),

    /**
     * 小数位数
     */
    DECIMAL_DIGITS("DECIMAL_DIGITS"),

    /**
     * 基数
     */
    NUM_PREC_RADIX("NUM_PREC_RADIX"),

    /**
     * 是否允许为null
     */
    NULLABLE("NULLABLE"),

    /**
     * 列描述
     */
    REMARKS("REMARKS"),

    /**
     * 默认值
     */
    COLUMN_DEF("COLUMN_DEF"),

    /**
     *
     */
    SQL_DATA_TYPE("SQL_DATA_TYPE"),

    /**
     *
     */
    SQL_DATETIME_SUB("SQL_DATETIME_SUB"),

    /**
     * 对于 char 类型，该长度是列中的最大字节数
     */
    CHAR_OCTET_LENGTH("CHAR_OCTET_LENGTH"),

    /**
     * 表中列的索引（从1开始）
     */
    ORDINAL_POSITION("ORDINAL_POSITION"),

    /**
     * ISO规则用来确定某一列的是否可为空(等同于NULLABLE的值:[ 0:'YES'; 1:'NO'; 2:''; ])
     * YES -- 该列可以有空值;
     * NO -- 该列不能为空;
     * 空字符串--- 不知道该列是否可为空
     */
    IS_NULLABLE("IS_NULLABLE"),

    /**
     *
     */
    SCOPE_CATALOG("SCOPE_CATALOG"),

    /**
     *
     */
    SCOPE_SCHEMA("SCOPE_SCHEMA"),

    /**
     *
     */
    SCOPE_TABLE("SCOPE_TABLE"),

    /**
     *
     */
    SOURCE_DATA_TYPE("SOURCE_DATA_TYPE"),

    /**
     * 是否自增
     */
    IS_AUTOINCREMENT("IS_AUTOINCREMENT"),

    /**
     * 是否为生成列
     */
    IS_GENERATEDCOLUMN("IS_GENERATEDCOLUMN");

    private final String value;

    ColumnMeta(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
