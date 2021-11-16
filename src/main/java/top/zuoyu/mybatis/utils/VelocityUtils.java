/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmil.com).
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
package top.zuoyu.mybatis.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.temp.mapper.BaseMapper;


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
        String packageName = ClassUtils.getPackageName(BaseMapper.class);
        String tableName = table.getTableName();
        List<Column> columns = new ArrayList<>(Collections.emptyList());
        Assert.notEmpty(table.getColumns(), "表" + tableName + "列数为0");
        columns.addAll(table.getColumns().stream().map(Column::builder).collect(Collectors.toList()));
        Set<String> primaryKeys = table.getPrimaryKeys();
        Assert.isTrue(primaryKeys.size() == 1, "表" + tableName + "主键错误");
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
    public static String getTemplate() {
        return "vm" + File.separator + "mapper.xml.vm";
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
         * JAVA字段名
         */
        private String javaField;

        /**
         * 是否自增（1是）
         */
        private String isIncrement;

        @NonNull
        public static Column builder(@NonNull top.zuoyu.mybatis.data.model.Column column) {
            Column newColumn = new Column();
            newColumn.setColumnName(column.getColumnName());
            newColumn.setJavaType(column.getDataType().getTypeName());
            newColumn.setJavaField(column.getColumnName());
            newColumn.setIsIncrement(YES.equalsIgnoreCase(column.getIsAutoincrement()) ? "1" : "0");
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

        public String getJavaField() {
            return javaField;
        }

        public void setJavaField(String javaField) {
            this.javaField = javaField;
        }

        public String getIsIncrement() {
            return isIncrement;
        }

        public void setIsIncrement(String isIncrement) {
            this.isIncrement = isIncrement;
        }
    }

}
