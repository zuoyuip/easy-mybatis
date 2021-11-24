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
package top.zuoyu.mybatis.ssist;

import java.io.Serializable;
import java.util.List;

import org.springframework.lang.NonNull;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.UnifyService;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * Mapper接口构建 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 15:45
 */
class MapperStructure {


    static Class<?> registerMapper(@NonNull Table table) {
        ClassPool classPool = ClassPool.getDefault();

        // 创建一个接口
        CtClass ctClass = classPool.makeInterface(Constant.MAPPER_PACKAGE_NAME + Constant.PACKAGE_SEPARATOR + String.format(Constant.MAPPER_SUFFIX, StrUtil.captureName(table.getTableName())));
        ctClass.setModifiers(Modifier.setPublic(Modifier.INTERFACE));


        try {

            CtClass unifyService = classPool.get(UnifyService.class.getTypeName());
            ctClass.addInterface(unifyService);

            CtClass listClass = classPool.get(List.class.getTypeName());
            CtClass jsonObjectClass = classPool.get(JsonObject.class.getTypeName());
            CtClass serializableClass = classPool.get(Serializable.class.getTypeName());
            CtClass serializableArrayClass = classPool.get(Serializable[].class.getTypeName());
            CtClass intClass = classPool.get(Integer.TYPE.getTypeName());


            // 创建方法

            // 查询所有
            CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{}, ctClass);
            ctClass.addMethod(selectList);

            // 根据已有键值查询
            CtMethod selectListByExample = new CtMethod(listClass, "selectListByExample", new CtClass[]{jsonObjectClass}, ctClass);
            ctClass.addMethod(selectListByExample);

            // 根据主键查询唯一对象
            CtMethod selectByPrimaryKey = new CtMethod(jsonObjectClass, "selectByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
            ctClass.addMethod(selectByPrimaryKey);

            // 新增对象
            CtMethod insert = new CtMethod(intClass, "insert", new CtClass[]{jsonObjectClass}, ctClass);
            ctClass.addMethod(insert);

            // 根据主键修改对象属性
            CtMethod updateByPrimaryKey = new CtMethod(intClass, "updateByPrimaryKey", new CtClass[]{jsonObjectClass}, ctClass);
            ctClass.addMethod(updateByPrimaryKey);

            // 根据主键删除对象
            CtMethod deleteByPrimaryKey = new CtMethod(intClass, "deleteByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
            ctClass.addMethod(deleteByPrimaryKey);

            // 批量根据主键删除对象
            CtMethod deleteByPrimaryKeys = new CtMethod(intClass, "deleteByPrimaryKeys", new CtClass[]{serializableArrayClass}, ctClass);
            ctClass.addMethod(deleteByPrimaryKeys);

        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }


        try {
            return classPool.toClass(ctClass);
        } catch (CannotCompileException e) {
            throw new CustomException("writeFile is fail!", e);
        }


    }
}
