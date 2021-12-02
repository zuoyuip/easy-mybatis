/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC中字段类型 .
 *
 * @author: zuoyu
 * @create: 2021-12-02 16:01
 */
public enum JdbcType {

    /**
     * ARRAY
     */
    ARRAY(Types.ARRAY, Array.class),

    /**
     * BIT
     */
    BIT(Types.BIT, Boolean.class),

    /**
     * TINYINT
     */
    TINYINT(Types.TINYINT, Byte.class),

    /**
     * SMALLINT
     */
    SMALLINT(Types.SMALLINT, Short.class),

    /**
     * INTEGER
     */
    INTEGER(Types.INTEGER, Integer.class),

    /**
     * BIGINT
     */
    BIGINT(Types.BIGINT, Long.class),

    /**
     * FLOAT
     */
    FLOAT(Types.FLOAT, Double.class),

    /**
     * REAL
     */
    REAL(Types.REAL, Float.class),

    /**
     * DOUBLE
     */
    DOUBLE(Types.DOUBLE, Double.class),

    /**
     * NUMERIC
     */
    NUMERIC(Types.NUMERIC, BigDecimal.class),

    /**
     * DECIMAL
     */
    DECIMAL(Types.DECIMAL, BigDecimal.class),

    /**
     * CHAR
     */
    CHAR(Types.CHAR, String.class),

    /**
     * VARCHAR
     */
    VARCHAR(Types.VARCHAR, String.class),

    /**
     * LONGVARCHAR
     */
    LONGVARCHAR(Types.LONGVARCHAR, String.class),

    /**
     * DATE
     */
    DATE(Types.DATE, Date.class),

    /**
     * TIME
     */
    TIME(Types.TIME, Date.class),

    /**
     * TIMESTAMP
     */
    TIMESTAMP(Types.TIMESTAMP, Date.class),

    /**
     * BINARY
     */
    BINARY(Types.BINARY, Byte[].class),

    /**
     * VARBINARY
     */
    VARBINARY(Types.VARBINARY, Byte[].class),

    /**
     * LONGVARBINARY
     */
    LONGVARBINARY(Types.LONGVARBINARY, Byte[].class),

    /**
     * NULL
     */
    NULL(Types.NULL, null),

    /**
     * OTHER
     */
    OTHER(Types.OTHER, Object.class),

    /**
     * BLOB
     */
    BLOB(Types.BLOB, Blob.class),

    /**
     * CLOB
     */
    CLOB(Types.CLOB, Clob.class),

    /**
     * BOOLEAN
     */
    BOOLEAN(Types.BOOLEAN, Boolean.class),

    /**
     * CURSOR
     */
    CURSOR(-10, Cursor.class), // Oracle

    /**
     * UNDEFINED
     */
    UNDEFINED(Integer.MIN_VALUE + 1000, null),

    /**
     * NVARCHAR
     */
    NVARCHAR(Types.NVARCHAR, String.class), // JDK6

    /**
     * NCHAR
     */
    NCHAR(Types.NCHAR, String.class), // JDK6

    /**
     * NCLOB
     */
    NCLOB(Types.NCLOB, String.class), // JDK6

    /**
     * STRUCT
     */
    STRUCT(Types.STRUCT, Struct.class),

    /**
     * JAVA_OBJECT
     */
    JAVA_OBJECT(Types.JAVA_OBJECT, Object.class),

    /**
     * DISTINCT
     */
    DISTINCT(Types.DISTINCT, Object.class),

    /**
     * REF
     */
    REF(Types.REF, Ref.class),

    /**
     * DATALINK
     */
    DATALINK(Types.DATALINK, URL.class),

    /**
     * ROWID
     */
    ROWID(Types.ROWID, RowId.class), // JDK6

    /**
     * LONGNVARCHAR
     */
    LONGNVARCHAR(Types.LONGNVARCHAR, String.class), // JDK6

    /**
     * SQLXML
     */
    SQLXML(Types.SQLXML, java.sql.SQLXML.class), // JDK6

    /**
     * DATETIMEOFFSET
     */
    DATETIMEOFFSET(-155, Date.class), // SQL Server 2008

    /**
     * TIME_WITH_TIMEZONE
     */
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE, Date.class), // JDBC 4.2 JDK8

    /**
     * TIMESTAMP_WITH_TIMEZONE
     */
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE, Date.class); // JDBC 4.2 JDK8

    private static final Map<Integer, JdbcType> CODE_LOOKUP = new HashMap<>();

    static {
        for (JdbcType type : JdbcType.values()) {
            CODE_LOOKUP.put(type.TYPE_CODE, type);
        }
    }

    public final int TYPE_CODE;
    public final Class<?> JAVA_TYPE;

    JdbcType(int code, Class<?> javaType) {
        this.TYPE_CODE = code;
        this.JAVA_TYPE = javaType;
    }

    public static JdbcType forCode(int code) {
        return CODE_LOOKUP.get(code);
    }

    public int getTypeCode() {
        return this.TYPE_CODE;
    }

    public Class<?> getJavaType() {
        return this.JAVA_TYPE;
    }
}
