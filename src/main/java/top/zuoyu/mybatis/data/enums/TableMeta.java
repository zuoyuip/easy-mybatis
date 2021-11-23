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

/**
 * 数据库表元数据 .
 *
 * @author: zuoyu
 * @create: 2021-10-16 17:06
 */
public enum TableMeta {

    /**
     * 表类别
     */
    TABLE_CAT("TABLE_CAT"),

    /**
     * 表模式（可能为空）,在oracle中获取的是命名空间
     */
    TABLE_SCHEM("TABLE_SCHEM"),

    /**
     * 表名
     */
    TABLE_NAME("TABLE_NAME"),

    /**
     * 表类型,典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"
     */
    TABLE_TYPE("TABLE_TYPE"),

    /**
     * 表备注
     */
    REMARKS("REMARKS"),

    /**
     * 类型
     */
    TYPE_CAT("TYPE_CAT"),

    /**
     * 类型方案
     */
    TYPE_SCHEM("TYPE_SCHEM"),

    /**
     * 类型名称
     */
    TYPE_NAME("TYPE_NAME"),

    /**
     * 自引用列名称
     */
    SELF_REFERENCING_COL_NAME("SELF_REFERENCING_COL_NAME"),

    /**
     * 生成参考
     */
    REF_GENERATION("REF_GENERATION");

    private final String value;

    TableMeta(String value) {
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
