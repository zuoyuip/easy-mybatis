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
package top.zuoyu.mybatis.data.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import top.zuoyu.mybatis.data.enums.TableMeta;

/**
 * 数据库表信息 .
 *
 * @author: zuoyu
 * @create: 2021-10-16 16:33
 */
public class Table implements Serializable, Cloneable {


    private static final long serialVersionUID = -4366205923949656662L;

    private static final String SPLIT_CHARS = ", ";

    /**
     * 主键列表
     */
    private final Set<String> primaryKeys = new LinkedHashSet<>();
    /**
     * 索引列表
     */
    private final List<Index> indexs = new LinkedList<>();
    /**
     * 所有列信息
     */
    private final Map<String, Column> columns = new LinkedHashMap<>();
    /**
     * 表类别
     */
    private String tableCat;
    /**
     * 表模式（可能为空）,在oracle中获取的是命名空间
     */
    private String tableSchema;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表类型,典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"
     */
    private String tableType;
    /**
     * 表备注
     */
    private String remarks;
    /**
     * 类型
     */
    private String typeCat;
    /**
     * 类型方案
     */
    private String typeSchema;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 自引用列名称
     */
    private String selfReferencingColName;
    /**
     * 生成参考
     */
    private String refGeneration;


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

    public String getTableType() {
        return this.tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTypeCat() {
        return this.typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchema() {
        return this.typeSchema;
    }

    public void setTypeSchema(String typeSchema) {
        this.typeSchema = typeSchema;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return this.selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return this.refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration = refGeneration;
    }

    public Set<String> getPrimaryKeys() {
        return this.primaryKeys;
    }

    /**
     * 添加主键名称
     *
     * @param primaryKey - 主键名称
     */
    public void addPrimaryKey(String primaryKey) {
        this.primaryKeys.add(primaryKey);
    }

    /**
     * 判断是否为主键
     *
     * @param columnName - 列名称
     * @return 是否为主键
     */
    public boolean isPrimaryKey(String columnName) {
        return this.primaryKeys.contains(columnName);
    }


    public Collection<Column> getColumns() {
        return columns.values();
    }

    /**
     * 添加列对象
     *
     * @param column - 列对象
     */
    public void addColumn(Column column) {
        this.columns.put(column.getColumnName(), column);
    }

    /**
     * 添加索引对象
     *
     * @param index - 索引对象
     */
    public void addIndex(Index index) {
        List<Index> indexList = this.indexs.stream().filter(item -> {
            String indexName = item.getIndexName();
            return Objects.nonNull(indexName) && indexName.equals(index.getIndexName());
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(indexList)) {
            this.indexs.add(index);
            return;
        }

        indexList.forEach(item -> item.setColumnName(item.getColumnName() + SPLIT_CHARS + index.getColumnName()));
    }

    /**
     * 获取某列信息
     *
     * @param columnName - 列名
     * @return this
     */
    public Column getColumn(String columnName) {
        return this.columns.get(columnName);
    }

    public List<Index> getIndexs() {
        return indexs;
    }

    /**
     * 通过tablesResultSet加载值
     *
     * @param tablesResultSet - 表元信息
     * @throws SQLException - 向上抛出
     */
    public void loadValuesByTablesResultSet(@NonNull ResultSet tablesResultSet) throws SQLException {
        this.setTableCat(tablesResultSet.getString(TableMeta.TABLE_CAT.value()));
        this.setTableSchema(tablesResultSet.getString(TableMeta.TABLE_SCHEM.value()));
        this.setTableName(tablesResultSet.getString(TableMeta.TABLE_NAME.value()));
        this.setTableType(tablesResultSet.getString(TableMeta.TABLE_TYPE.value()));
        this.setRemarks(tablesResultSet.getString(TableMeta.REMARKS.value()));
        this.setTypeCat(tablesResultSet.getString(TableMeta.TYPE_CAT.value()));
        this.setTypeSchema(tablesResultSet.getString(TableMeta.TYPE_SCHEM.value()));
        this.setTypeName(tablesResultSet.getString(TableMeta.TYPE_NAME.value()));
        this.setSelfReferencingColName(tablesResultSet.getString(TableMeta.SELF_REFERENCING_COL_NAME.value()));
        this.setRefGeneration(tablesResultSet.getString(TableMeta.REF_GENERATION.value()));
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableCat='" + tableCat + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", typeCat='" + typeCat + '\'' +
                ", typeSchema='" + typeSchema + '\'' +
                ", typeName='" + typeName + '\'' +
                ", selfReferencingColName='" + selfReferencingColName + '\'' +
                ", refGeneration='" + refGeneration + '\'' +
                '}';
    }
}
