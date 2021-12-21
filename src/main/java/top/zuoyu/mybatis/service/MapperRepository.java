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
package top.zuoyu.mybatis.service;

import java.io.Serializable;
import java.util.List;

import top.zuoyu.mybatis.json.JsonArray;
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
     *
     * @return 所有数据
     */
    List<JsonObject> selectList();

    /**
     * 根据已有键值查询
     *
     * @param example - 已有键值
     * @return 符合要求的数据集合
     */
    List<JsonObject> selectListByExample(JsonObject example);


    /**
     * 根据主键查询唯一对象
     *
     * @param primaryKey - 主键
     * @return 唯一对象
     */
    JsonObject selectByPrimaryKey(Serializable primaryKey);

    /**
     * 查询符合条件的数据
     *
     * @param suffixSql - 条件语句（例如：where field = xxx）
     * @return 数据数量
     */
    List<JsonObject> selectListBy(String suffixSql);

    /**
     * 查询特定的字段或结果
     * （例如："fieldA, fieldB, fieldC"）
     * （例如："COUNT(field)"）
     *
     * @param fields - 特定的字段或结果
     * @return 特定的字段或结果
     */
    JsonArray selectFields(String fields);

    /**
     * 根据已有键值查询特定的字段或结果
     * （例如："fieldA, fieldB, fieldC"）
     * （例如："COUNT(field)"）
     *
     * @param fields  - 特定的字段或结果
     * @param example - 已有键值
     * @return 特定的字段或结果
     */
    JsonArray selectFieldsByExample(String fields, JsonObject example);

    /**
     * 根据主键查询特定的字段或结果
     *
     * @param fields     - 特定的字段或结果（例如："fieldA, fieldB, fieldC"）
     * @param primaryKey - 主键
     * @return 特定的字段或结果
     */
    JsonArray selectFieldsByPrimaryKey(String fields, Serializable primaryKey);

    /**
     * 根据主键集合查询特定的字段或结果
     *
     * @param fields      - 特定的字段或结果（例如："fieldA, fieldB, fieldC"）
     * @param primaryKeys - 主键集合
     * @return 特定的字段或结果
     */
    JsonArray selectFieldsByPrimaryKeys(String fields, Serializable[] primaryKeys);

    /**
     * 查询符合条件的数据
     *
     * @param fields    - 特定的字段或结果（例如："fieldA, fieldB, fieldC"）
     * @param suffixSql - 条件语句（例如：where field = xxx）
     * @return 特定的字段或结果
     */
    JsonArray selectFieldsBy(String fields, String suffixSql);

    /**
     * 查询符合条件的数据数量
     *
     * @param suffixSql - 条件语句（例如：where field = xxx）
     * @return 数据数量
     */
    long countBy(String suffixSql);

    /**
     * 根据已有键值查询是否存在符合条件的数据数量
     *
     * @param example - 已有键值
     * @return 数据数量
     */
    long countByExample(JsonObject example);

    /**
     * 是否存在符合条件的数据
     *
     * @param suffixSql - 条件语句（例如：where field = xxx）
     * @return 是否存在
     */
    boolean existsBy(String suffixSql);

    /**
     * 根据已有键值查询是否存在符合条件的数据
     *
     * @param example - 已有键值
     * @return 是否存在
     */
    boolean existsByExample(JsonObject example);

    /**
     * 新增对象
     *
     * @param jsonObject - 对象键值
     * @return 变动数据的数量
     */
    int insert(JsonObject jsonObject);

    /**
     * 批量新增对象
     *
     * @param jsonObjects - 对象键值集合
     * @return 变动数据的数量
     */
    int insertBatch(List<JsonObject> jsonObjects);

    /**
     * 根据主键修改对象属性
     *
     * @param jsonObject - 包含主键对象键值
     * @return 变动数据的数量
     */
    int updateByPrimaryKey(JsonObject jsonObject);

    /**
     * 修改特定条件的对象属性
     *
     * @param jsonObject - 要修改的键值
     * @param suffixSql  - 条件语句（例如：where field = xxx）
     * @return 变动数据的数量
     */
    int updateBy(JsonObject jsonObject, String suffixSql);

    /**
     * 批量根据主键修改对象属性
     *
     * @param jsonObjects - 对象键值集合
     * @return 变动数据的数量
     */
    int updateByPrimaryKeyBatch(List<JsonObject> jsonObjects);

    /**
     * 根据主键删除对象
     *
     * @param primaryKey - 主键
     * @return 变动数据的数量
     */
    int deleteByPrimaryKey(Serializable primaryKey);

    /**
     * 删除符合条件的数据
     *
     * @param suffixSql - 条件语句（例如：where field = xxx）
     * @return 变动数据的数量
     */
    int deleteBy(String suffixSql);

    /**
     * 批量根据主键删除对象
     *
     * @param primaryKeys - 主键组
     * @return 变动数据的数量
     */
    int deleteByPrimaryKeys(Serializable[] primaryKeys);

    /**
     * 根据已有键值删除对象
     *
     * @param example - 已有键值
     * @return 变动数据的数量
     */
    int deleteByExample(JsonObject example);
}
