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
import org.springframework.core.io.Resource;

/**
 * Mybatis配置 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 12:25
 */
@ConfigurationProperties(prefix = EasyProperties.EASY_PREFIX)
public class EasyProperties {

    public static final String EASY_PREFIX = "easy-mybatis";

    private Resource[] resources;

    private String[] tableNames;

    private Map<String, String> sequences;

    private static String dateFormat;

    private String oracleDateFormat;

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

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
