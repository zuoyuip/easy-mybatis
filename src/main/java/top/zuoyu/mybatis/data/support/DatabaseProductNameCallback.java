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
import java.util.Objects;

import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.DataInfoLoad;

/**
 * 获取表名 .
 *
 * @author: zuoyu
 * @create: 2021-11-04 10:00
 */
public class DatabaseProductNameCallback extends DataInfoLoad implements org.springframework.jdbc.support.DatabaseMetaDataCallback<String> {


    private volatile static DatabaseProductNameCallback DATABASE_PRODUCT_NAME_CALLBACK;

    private DatabaseProductNameCallback() {
    }

    public static DatabaseProductNameCallback getInstance() {
        if (Objects.isNull(DATABASE_PRODUCT_NAME_CALLBACK)) {
            synchronized (DatabaseProductNameCallback.class) {
                if (Objects.isNull(DATABASE_PRODUCT_NAME_CALLBACK)) {
                    DATABASE_PRODUCT_NAME_CALLBACK = new DatabaseProductNameCallback();
                }
            }
        }
        return DATABASE_PRODUCT_NAME_CALLBACK;
    }

    @Override
    public String processMetaData(@NonNull DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
        return getDatabaseProductName(databaseMetaData);
    }
}
