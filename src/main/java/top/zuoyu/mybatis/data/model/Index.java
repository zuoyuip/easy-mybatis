package top.zuoyu.mybatis.data.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.lang.NonNull;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import top.zuoyu.mybatis.data.enums.IndexMeta;
import top.zuoyu.mybatis.exception.CustomException;

/**
 * 表索引 .
 *
 * @author: zuoyu
 * @create: 2021-10-25 22:15
 */
public class Index implements Serializable, Cloneable {

    private static final long serialVersionUID = -4057191462039692611L;

    /**
     * 表类别
     */
    @JsonInclude(value = Include.ALWAYS)
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
     * 索引值是否可以不唯一
     */
    private boolean nonUnique;

    /**
     * 索引类别
     */
    private String indexQualifier;

    /**
     * 索引的名称
     */
    private String indexName;

    /**
     * 索引类型
     */
    private String type;

    /**
     * 在索引列顺序号
     */
    private String ordinalPosition;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列排序顺序:升序还是降序[A:升序; B:降序];
     */
    private String ascOrDesc;

    /**
     * 基数
     */
    private String cardinality;

    /**
     * 页数
     */
    private String pages;

    /**
     * 过滤器条件
     */
    private String filterCondition;

    public Index() {
    }

    public Index(Table table, ResultSet resultSet) {
        try {
            this.init(table, resultSet);
        } catch (SQLException e) {
            throw new CustomException(e.getMessage(), e);
        }
    }

    /**
     * 创建索引对象
     *
     * @param table     - 表信息
     * @param resultSet - 索引元信息
     * @return 索引对象
     */
    @NonNull
    public static Index create(Table table, ResultSet resultSet) {
        return new Index(table, resultSet);
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    public String getIndexQualifier() {
        return indexQualifier;
    }

    public void setIndexQualifier(String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(String ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(String ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    private void init(@NonNull Table table, @NonNull ResultSet resultSet) throws SQLException {
        this.setTableCat(resultSet.getString(IndexMeta.TABLE_CAT.value()));
        this.setTableSchema(resultSet.getString(IndexMeta.TABLE_SCHEM.value()));
        this.setTableName(resultSet.getString(IndexMeta.TABLE_NAME.value()));
        this.setNonUnique(resultSet.getBoolean(IndexMeta.NON_UNIQUE.value()));
        this.setIndexQualifier(resultSet.getString(IndexMeta.INDEX_QUALIFIER.value()));
        this.setIndexName(resultSet.getString(IndexMeta.INDEX_NAME.value()));
        this.setType(resultSet.getString(IndexMeta.TYPE.value()));
        this.setOrdinalPosition(resultSet.getString(IndexMeta.ORDINAL_POSITION.value()));
        this.setColumnName(resultSet.getString(IndexMeta.COLUMN_NAME.value()));
        this.setAscOrDesc(resultSet.getString(IndexMeta.ASC_OR_DESC.value()));
        this.setCardinality(resultSet.getString(IndexMeta.CARDINALITY.value()));
        this.setPages(resultSet.getString(IndexMeta.PAGES.value()));
        this.setFilterCondition(resultSet.getString(IndexMeta.FILTER_CONDITION.value()));
        table.addIndex(this);
    }

    @Override
    public String toString() {
        return "Index{" +
                "tableCat='" + tableCat + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", nonUnique='" + nonUnique + '\'' +
                ", indexQualifier='" + indexQualifier + '\'' +
                ", indexName='" + indexName + '\'' +
                ", type='" + type + '\'' +
                ", ordinalPosition='" + ordinalPosition + '\'' +
                ", columnName='" + columnName + '\'' +
                ", ascOrDesc='" + ascOrDesc + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", pages='" + pages + '\'' +
                ", filterCondition='" + filterCondition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Index)) {
            return false;
        }
        Index index = (Index) o;
        return Objects.equals(getTableCat(), index.getTableCat()) && Objects.equals(getTableSchema(), index.getTableSchema()) && Objects.equals(getTableName(), index.getTableName()) && Objects.equals(isNonUnique(), index.isNonUnique()) && Objects.equals(getIndexQualifier(), index.getIndexQualifier()) && Objects.equals(getIndexName(), index.getIndexName()) && Objects.equals(getType(), index.getType()) && Objects.equals(getOrdinalPosition(), index.getOrdinalPosition()) && Objects.equals(getColumnName(), index.getColumnName()) && Objects.equals(getAscOrDesc(), index.getAscOrDesc()) && Objects.equals(getCardinality(), index.getCardinality()) && Objects.equals(getPages(), index.getPages()) && Objects.equals(getFilterCondition(), index.getFilterCondition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableCat(), getTableSchema(), getTableName(), isNonUnique(), getIndexQualifier(), getIndexName(), getType(), getOrdinalPosition(), getColumnName(), getAscOrDesc(), getCardinality(), getPages(), getFilterCondition());
    }
}
