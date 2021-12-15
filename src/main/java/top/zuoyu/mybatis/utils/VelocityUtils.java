/*
 * MIT License
 *
 * Copyright (c) 2021 zuoyuip@foxmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package top.zuoyu.mybatis.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.enums.JdbcType;
import top.zuoyu.mybatis.data.model.Table;


/**
 * 模板处理工具类
 *
 * @author zuoyu
 */
public class VelocityUtils {

    private static final String YES = "YES";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    @NonNull
    public static VelocityContext prepareContext(@NonNull Table table) {
        String packageName = Constant.MAPPER_PACKAGE_NAME;
        String tableName = table.getTableName();
        List<Column> columns = new ArrayList<>(Collections.emptyList());
        Assert.notEmpty(table.getColumns(), "table\t" + tableName + "column count is 0");
        columns.addAll(table.getColumns().stream().map(Column::builder).collect(Collectors.toList()));
        Set<String> primaryKeys = table.getPrimaryKeys();
        Assert.isTrue(primaryKeys.size() == 1, "table\t" + tableName + "primaryKey is error");
        String primaryKey = primaryKeys.iterator().next();
        top.zuoyu.mybatis.data.model.Column primaryKeyColumn = table.getColumn(primaryKey);

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", tableName);
        velocityContext.put("className", StrUtil.captureName(tableName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("pkColumn", Column.builder(primaryKeyColumn));
        velocityContext.put("columns", columns);
        return velocityContext;
    }


    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    @NonNull
    public static String getMySqlTemplate() {
        return "vm/mysql/mapper.xml.vm";
    }

    public static class Column {
        /**
         * 列名称
         */
        private String columnName;

        /**
         * JAVA类型
         */
        private String javaType;

        /**
         * JDBC类型
         */
        private String jdbcType;

        /**
         * JAVA字段名
         */
        private String javaField;

        /**
         * 是否自增（1是）
         */
        private String increment;

        /**
         * 是否必须
         */
        private String required;

        @NonNull
        public static Column builder(@NonNull top.zuoyu.mybatis.data.model.Column column) {
            Column newColumn = new Column();
            newColumn.setColumnName(column.getColumnName());
            newColumn.setJavaType(column.getDataType().getTypeName());
            newColumn.setJavaField(column.getColumnName());
            String sqlDataType = column.getSqlDataType();
            newColumn.setJdbcType(JdbcType.forCode(Integer.parseInt(sqlDataType)).name());
            newColumn.setIncrement(YES.equalsIgnoreCase(column.getIsAutoincrement()) ? "1" : "0");
            newColumn.setRequired(YES.equalsIgnoreCase(column.getNullable()) ? "0" : "1");
            return newColumn;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public String getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(String jdbcType) {
            this.jdbcType = jdbcType;
        }

        public String getJavaField() {
            return javaField;
        }

        public void setJavaField(String javaField) {
            this.javaField = javaField;
        }

        public String getIncrement() {
            return increment;
        }

        public void setIncrement(String increment) {
            this.increment = increment;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }
    }

}
