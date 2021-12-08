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
 * 元信息内表类型 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 14:09
 */
public enum TableType {

    /**
     * 表
     */
    TABLE("TABLE"),

    /**
     * 视图
     */
    VIEW("VIEW"),
    SYSTEM_TABLE ("SYSTEM TABLE"),
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    ALIAS("ALIAS"),
    SYNONYM("SYNONYM");

    private final String value;

    /**
     * 构造
     * @param value 值
     */
    TableType(String value){
        this.value = value;
    }
    /**
     * 获取值
     * @return 值
     */
    public String value(){
        return this.value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
