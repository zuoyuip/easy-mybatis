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
