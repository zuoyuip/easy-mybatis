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
package top.zuoyu.mybatis.data.enums;

/**
 * 主键元数据 .
 *
 * @author: zuoyu
 * @create: 2021-10-25 22:05
 */
public enum IndexMeta {

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
     * 索引值是否可以不唯一
     */
    NON_UNIQUE("NON_UNIQUE"),

    /**
     * 索引类别
     */
    INDEX_QUALIFIER("INDEX_QUALIFIER"),

    /**
     * 索引的名称
     */
    INDEX_NAME("INDEX_NAME"),

    /**
     * 索引类型
     */
    TYPE("TYPE"),

    /**
     * 在索引列顺序号
     */
    ORDINAL_POSITION("ORDINAL_POSITION"),

    /**
     * 列名
     */
    COLUMN_NAME("COLUMN_NAME"),

    /**
     * 列排序顺序:升序还是降序[A:升序; B:降序];
     */
    ASC_OR_DESC("ASC_OR_DESC"),

    /**
     * 基数
     */
    CARDINALITY("CARDINALITY"),

    /**
     * 页数
     */
    PAGES("PAGES"),

    /**
     * 过滤器条件
     */
    FILTER_CONDITION("FILTER_CONDITION");

    private final String value;

    IndexMeta(String value) {
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
