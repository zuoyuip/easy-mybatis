package top.zuoyu.mybatis.data.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.enums.ColumnMeta;
import top.zuoyu.mybatis.exception.CustomException;

/**
 * 数据库列信息 .
 *
 * @author: zuoyu
 * @create: 2021-10-16 16:38
 */
public class Column implements Serializable, Cloneable {


    private static final long serialVersionUID = -8107550134001981283L;

    /**
     * 表类别
     */
    private String tableCat;

    /**
     * 表模式,在oracle中获取的是命名空间
     */
    private String tableSchema;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 对应的java.sql.Types的SQL类型(列类型ID)
     */
    private String dataType;

    /**
     * java.sql.Types类型名称(列类型名称)
     */
    private String typeName;

    /**
     * 列大小
     */
    private Integer columnSize;

    /**
     * 缓冲长度
     */
    private Long bufferLength;

    /**
     * 小数位数
     */
    private String decimalDigits;

    /**
     * 基数
     */
    private String numPrecRadix;

    /**
     * 是否允许为null
     */
    private String nullable;

    /**
     * 列描述
     */
    private String remarks;

    /**
     * 默认值
     */
    private String columnDef;

    /**
     *
     */
    private String sqlDataType;

    /**
     *
     */
    private String sqlDatetimeSub;

    /**
     * 对于 char 类型，该长度是列中的最大字节数
     */
    private String charOctetLength;

    /**
     * 表中列的索引（从1开始）
     */
    private String ordinalPosition;

    /**
     * ISO规则用来确定某一列的是否可为空(等同于NULLABLE的值:[ 0:'YES'; 1:'NO'; 2:''; ])
     * YES -- 该列可以有空值;
     * NO -- 该列不能为空;
     * 空字符串--- 不知道该列是否可为空
     */
    private String isNullable;

    /**
     *
     */
    private String scopeCatalog;

    /**
     *
     */
    private String scopeSchema;

    /**
     *
     */
    private String scopeTable;

    /**
     *
     */
    private String sourceDataType;

    /**
     * 是否自增
     */
    private String isAutoincrement;

    /**
     * 是否为生成列
     */
    private String isGeneratedColumn;

    /**
     * 是否为主键
     */
    private boolean isPrimaryKey;

    public Column() {
    }

    private Column(Table table, ResultSet resultSet) {
        try {
            this.init(table, resultSet);
        } catch (SQLException e) {
            throw new CustomException(e.getMessage(), e);
        }
    }

    /**
     * 创建列对象
     *
     * @param table     - 表信息
     * @param resultSet - 列元信息
     * @return 列对象
     */
    @NonNull
    public static Column create(Table table, ResultSet resultSet) {
        return new Column(table, resultSet);
    }

    public String getTableCat() {
        return this.tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchema() {
        return this.tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Long getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(Long bufferLength) {
        this.bufferLength = bufferLength;
    }

    public String getDecimalDigits() {
        return this.decimalDigits;
    }

    public void setDecimalDigits(String decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getNumPrecRadix() {
        return this.numPrecRadix;
    }

    public void setNumPrecRadix(String numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public String getNullable() {
        return this.nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getColumnDef() {
        return this.columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public String getSqlDataType() {
        return this.sqlDataType;
    }

    public void setSqlDataType(String sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public String getSqlDatetimeSub() {
        return this.sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(String sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public String getCharOctetLength() {
        return this.charOctetLength;
    }

    public void setCharOctetLength(String charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public String getOrdinalPosition() {
        return this.ordinalPosition;
    }

    public void setOrdinalPosition(String ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return this.isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getScopeCatalog() {
        return this.scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    public String getScopeSchema() {
        return this.scopeSchema;
    }

    public void setScopeSchema(String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    public String getScopeTable() {
        return this.scopeTable;
    }

    public void setScopeTable(String scopeTable) {
        this.scopeTable = scopeTable;
    }

    public String getSourceDataType() {
        return this.sourceDataType;
    }

    public void setSourceDataType(String sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getIsAutoincrement() {
        return this.isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    public String getIsGeneratedColumn() {
        return this.isGeneratedColumn;
    }

    public void setIsGeneratedColumn(String isGeneratedColumn) {
        this.isGeneratedColumn = isGeneratedColumn;
    }

    public boolean isPrimaryKey() {
        return this.isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.isPrimaryKey = primaryKey;
    }

    private void init(@NonNull Table table, @NonNull ResultSet resultSet) throws SQLException {
        this.setTableCat(resultSet.getString(ColumnMeta.TABLE_CAT.value()));
        this.setTableSchema(resultSet.getString(ColumnMeta.TABLE_SCHEM.value()));
        this.setTableName(resultSet.getString(ColumnMeta.TABLE_NAME.value()));
        this.setColumnName(resultSet.getString(ColumnMeta.COLUMN_NAME.value()));
        this.setDataType(resultSet.getString(ColumnMeta.DATA_TYPE.value()));
        this.setTypeName(resultSet.getString(ColumnMeta.TYPE_NAME.value()));
        this.setColumnSize(resultSet.getInt(ColumnMeta.COLUMN_SIZE.value()));
        this.setBufferLength(resultSet.getLong(ColumnMeta.BUFFER_LENGTH.value()));
        this.setDecimalDigits(resultSet.getString(ColumnMeta.DECIMAL_DIGITS.value()));
        this.setNumPrecRadix(resultSet.getString(ColumnMeta.NUM_PREC_RADIX.value()));
        this.setNullable(resultSet.getString(ColumnMeta.NULLABLE.value()));
        this.setRemarks(resultSet.getString(ColumnMeta.REMARKS.value()));
        this.setColumnDef(resultSet.getString(ColumnMeta.COLUMN_DEF.value()));
        this.setSqlDataType(resultSet.getString(ColumnMeta.SQL_DATA_TYPE.value()));
        this.setSqlDatetimeSub(resultSet.getString(ColumnMeta.SQL_DATETIME_SUB.value()));
        this.setCharOctetLength(resultSet.getString(ColumnMeta.CHAR_OCTET_LENGTH.value()));
        this.setOrdinalPosition(resultSet.getString(ColumnMeta.ORDINAL_POSITION.value()));
        this.setIsNullable(resultSet.getString(ColumnMeta.IS_NULLABLE.value()));
        this.setScopeCatalog(resultSet.getString(ColumnMeta.SCOPE_CATALOG.value()));
        this.setScopeSchema(resultSet.getString(ColumnMeta.SCOPE_SCHEMA.value()));
        this.setScopeTable(resultSet.getString(ColumnMeta.SCOPE_TABLE.value()));
        this.setSourceDataType(resultSet.getString(ColumnMeta.SOURCE_DATA_TYPE.value()));
        this.setIsAutoincrement(resultSet.getString(ColumnMeta.IS_AUTOINCREMENT.value()));
        this.setIsGeneratedColumn(resultSet.getString(ColumnMeta.IS_GENERATEDCOLUMN.value()));
        this.setPrimaryKey(table.isPrimaryKey(this.columnName));
        table.addColumn(this);
    }

    @Override
    public String toString() {
        return "Column{" +
                "tableCat='" + tableCat + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", typeName='" + typeName + '\'' +
                ", columnSize='" + columnSize + '\'' +
                ", bufferLength='" + bufferLength + '\'' +
                ", decimalDigits='" + decimalDigits + '\'' +
                ", numPrecRadix='" + numPrecRadix + '\'' +
                ", nullable='" + nullable + '\'' +
                ", remarks='" + remarks + '\'' +
                ", columnDef='" + columnDef + '\'' +
                ", sqlDataType='" + sqlDataType + '\'' +
                ", sqlDatetimeSub='" + sqlDatetimeSub + '\'' +
                ", charOctetLength='" + charOctetLength + '\'' +
                ", ordinalPosition='" + ordinalPosition + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", scopeCatalog='" + scopeCatalog + '\'' +
                ", scopeSchema='" + scopeSchema + '\'' +
                ", scopeTable='" + scopeTable + '\'' +
                ", sourceDataType='" + sourceDataType + '\'' +
                ", isAutoincrement='" + isAutoincrement + '\'' +
                ", isGeneratedColumn='" + isGeneratedColumn + '\'' +
                ", isPrimaryKey=" + isPrimaryKey +
                '}';
    }
}
