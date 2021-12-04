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
package top.zuoyu.mybatis.service;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.zuoyu.mybatis.json.JsonObject;

/**
 * 统一接口 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 17:33
 */
public interface MapperRepository {

    /**
     * 查询所有
     * @return 所有数据
     */
    List<JsonObject> selectList();

    /**
     * 根据已有键值查询
     * @param jsonObject - 已有键值
     * @return 符合要求的数据集合
     */
    List<JsonObject> selectListByExample(JsonObject jsonObject);


    /**
     * 根据主键查询唯一对象
     * @param primaryKey - 主键
     * @return 唯一对象
     */
    JsonObject selectByPrimaryKey(Serializable primaryKey);


    /**
     * 新增对象
     * @param jsonObject - 对象键值
     * @return 结果
     */
    int insert(JsonObject jsonObject);

    /**
     * 批量新增对象
     * @param jsonObjects - 对象键值集合
     * @return 结果
     */
    int insertBatch(@Param("list") List<JsonObject> jsonObjects);

    /**
     * 根据主键修改对象属性
     * @param jsonObject - 包含主键对象键值
     * @return 结果
     */
    int updateByPrimaryKey(JsonObject jsonObject);

    /**
     * 批量根据主键修改对象属性
     * @param jsonObjects - 对象键值集合
     * @return 结果
     */
    int updateByPrimaryKeyBatch(@Param("list") List<JsonObject> jsonObjects);

    /**
     * 根据主键删除对象
     * @param primaryKey - 主键
     * @return 结果
     */
    int deleteByPrimaryKey(Serializable primaryKey);

    /**
     * 批量根据主键删除对象
     * @param primaryKeys - 主键组
     * @return 结果
     */
    int deleteByPrimaryKeys(@Param("array") Serializable[] primaryKeys);
}
