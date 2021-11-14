package top.zuoyu.mybatis.service;

import java.util.List;

import top.zuoyu.mybatis.json.JsonObject;

/**
 * 统一接口 .
 *
 * @author: zuoyu
 * @create: 2021-11-02 17:33
 */
public interface UnifyService {

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


}
