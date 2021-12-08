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
package top.zuoyu.mybatis.data.support;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.support.MetaDataAccessException;

import top.zuoyu.mybatis.data.DataInfoLoad;
import top.zuoyu.mybatis.data.model.Table;

/**
 * 获取表信息 .
 *
 * @author: zuoyu
 * @create: 2021-11-05 09:41
 */
public class TablesCallback extends DataInfoLoad implements org.springframework.jdbc.support.DatabaseMetaDataCallback<List<Table>> {

    private volatile static TablesCallback TABLES_CALLBACK;

    public static TablesCallback getInstance() {
        if (Objects.isNull(TABLES_CALLBACK)) {
            synchronized (TablesCallback.class) {
                if (Objects.isNull(TABLES_CALLBACK)) {
                    TABLES_CALLBACK = new TablesCallback();
                }
            }
        }
        return TABLES_CALLBACK;
    }

    @Override
    public List<Table> processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
        return getTables(databaseMetaData);
    }
}
