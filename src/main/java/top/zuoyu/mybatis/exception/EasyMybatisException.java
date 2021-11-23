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
package top.zuoyu.mybatis.exception;

/**
 * EasyMybatis异常 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 15:03
 */
public class EasyMybatisException extends CustomException {

    private static final long serialVersionUID = -6192276367409830243L;

    public EasyMybatisException(String message) {
        super(message);
    }

    public EasyMybatisException(String message, Integer code) {
        super(message, code);
    }

    public EasyMybatisException(String message, Throwable e) {
        super(message, e);
    }
}
