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
