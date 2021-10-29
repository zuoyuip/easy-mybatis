package top.zuoyu.mybatis.data.enums;

import java.awt.Cursor;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.Struct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JDBC中字段类型枚举 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 12:13
 */
public enum JdbcType {

    ARRAY(java.sql.Types.ARRAY, Array.class), //
    BIT(java.sql.Types.BIT, Boolean.class), //
    TINYINT(java.sql.Types.TINYINT, Byte.class), //
    SMALLINT(java.sql.Types.SMALLINT, Short.class), //
    INTEGER(java.sql.Types.INTEGER, Integer.class), //
    BIGINT(java.sql.Types.BIGINT, Long.class), //
    FLOAT(java.sql.Types.FLOAT, Double.class), //
    REAL(java.sql.Types.REAL, Float.class), //
    DOUBLE(java.sql.Types.DOUBLE, Double.class), //
    NUMERIC(java.sql.Types.NUMERIC, BigDecimal.class), //
    DECIMAL(java.sql.Types.DECIMAL, BigDecimal.class), //
    CHAR(java.sql.Types.CHAR, String.class), //
    VARCHAR(java.sql.Types.VARCHAR, String.class), //
    LONGVARCHAR(java.sql.Types.LONGVARCHAR, String.class), //
    DATE(java.sql.Types.DATE, Date.class), //
    TIME(java.sql.Types.TIME, Date.class), //
    TIMESTAMP(java.sql.Types.TIMESTAMP, Date.class), //
    BINARY(java.sql.Types.BINARY, Byte[].class), //
    VARBINARY(java.sql.Types.VARBINARY, Byte[].class), //
    LONGVARBINARY(java.sql.Types.LONGVARBINARY, Byte[].class), //
    NULL(java.sql.Types.NULL, null), //
    OTHER(java.sql.Types.OTHER, Object.class), //
    BLOB(java.sql.Types.BLOB, Blob.class), //
    CLOB(java.sql.Types.CLOB, Clob.class), //
    BOOLEAN(java.sql.Types.BOOLEAN, Boolean.class), //
    CURSOR(-10, Cursor.class), // Oracle
    UNDEFINED(Integer.MIN_VALUE + 1000, null), //
    NVARCHAR(java.sql.Types.NVARCHAR, String.class), // JDK6
    NCHAR(java.sql.Types.NCHAR, String.class), // JDK6
    NCLOB(java.sql.Types.NCLOB, String.class), // JDK6
    STRUCT(java.sql.Types.STRUCT, Struct.class), //
    JAVA_OBJECT(java.sql.Types.JAVA_OBJECT, Object.class), //
    DISTINCT(java.sql.Types.DISTINCT, Object.class), //
    REF(java.sql.Types.REF, Ref.class), //
    DATALINK(java.sql.Types.DATALINK, URL.class), //
    ROWID(java.sql.Types.ROWID, RowId.class), // JDK6
    LONGNVARCHAR(java.sql.Types.LONGNVARCHAR, String.class), // JDK6
    SQLXML(java.sql.Types.SQLXML, java.sql.SQLXML.class), // JDK6
    TIME_WITH_TIMEZONE(2013, Date.class), // JDBC 4.2 JDK8
    TIMESTAMP_WITH_TIMEZONE(2014, Date.class); // JDBC 4.2 JDK8

    private static final Map<Integer, JdbcType> CODE_MAP = new ConcurrentHashMap<>(100, 1);

    static {
        for (JdbcType type : JdbcType.values()) {
            CODE_MAP.put(type.typeCode, type);
        }
    }

    public final int typeCode;
    public final Class<?> javaType;

    /**
     * 构造
     *
     * @param code     {@link java.sql.Types} 中对应的值
     * @param javaType
     */
    JdbcType(int code, Class<?> javaType) {
        this.typeCode = code;
        this.javaType = javaType;
    }

    /**
     * 通过{@link java.sql.Types}中对应int值找到enum值
     *
     * @param code Jdbc type值
     * @return {@link JdbcType}
     */
    public static JdbcType valueOf(int code) {
        return CODE_MAP.get(code);
    }

    public int getTypeCode() {
        return typeCode;
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}
