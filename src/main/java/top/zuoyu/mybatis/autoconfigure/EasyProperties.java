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
package top.zuoyu.mybatis.autoconfigure;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 12:25
 */
@ConfigurationProperties(prefix = EasyProperties.EASY_PREFIX)
public class EasyProperties {

    public static final String EASY_PREFIX = "easy-mybatis";

    /**
     * 从数据库读取日期时自动格式化的日期格式，如果不配置默认为"yyyy-MM-dd HH:mm:ss"
     */
    private static String dateFormat;
    /**
     * 需要载入的表名，多表名用逗号隔开
     */
    private String[] tableNames;
    /**
     * Oracle数据库需要配置的主键生成器
     */
    private Map<String, String> sequences;
    /**
     * Oracle数据库解析日期时的日期格式，如果不配置默认为"yyyy-mm-dd hh24:mi:ss"
     */
    private String oracleDateFormat;

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public Map<String, String> getSequences() {
        return sequences;
    }

    public void setSequences(Map<String, String> sequences) {
        this.sequences = sequences;
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        EasyProperties.dateFormat = dateFormat;
    }

    public String getOracleDateFormat() {
        return oracleDateFormat;
    }

    public void setOracleDateFormat(String oracleDateFormat) {
        this.oracleDateFormat = oracleDateFormat;
    }
}
