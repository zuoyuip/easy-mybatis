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

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.json.JsonArray;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.MapperRepository;
import top.zuoyu.mybatis.utils.ClassUtil;
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
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        // 接口注解
        AnnotationsAttribute classAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation interfaceAnn = new Annotation(Repository.class.getTypeName(), constPool);
        classAttr.addAnnotation(interfaceAnn);
        classFile.addAttribute(classAttr);

        try {

            CtClass unifyService = classPool.get(MapperRepository.class.getTypeName());
            ctClass.addInterface(unifyService);

            // 创建方法
            methodList(ctClass, classPool);

        } catch (NotFoundException | CannotCompileException e) {
            throw new EasyMybatisException(e.getMessage(), e);
        }

        try {
            return classPool.toClass(ctClass);
        } catch (CannotCompileException e) {
            throw new EasyMybatisException("toClass is fail!", e);
        }


    }

    private static void param(@NonNull CtMethod ctMethod, @NonNull String... values) {
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        ConstPool constPool = methodInfo.getConstPool();
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
        Annotation[][] paramArrays = new Annotation[values.length][1];
        for (int i = 0; i < values.length; i++) {
            if (!StringUtils.hasLength(values[i])) {
                paramArrays[i][0] = new Annotation(NonNull.class.getTypeName(), constPool);
                continue;
            }
            Annotation annotation = new Annotation(Param.class.getTypeName(), constPool);
            annotation.addMemberValue("value", new StringMemberValue(values[i], constPool));
            paramArrays[i][0] = annotation;
        }
        parameterAnnotationsAttribute.setAnnotations(paramArrays);
        methodInfo.addAttribute(parameterAnnotationsAttribute);
    }

    private static void methodList(@NonNull CtClass ctClass, @NonNull ClassPool classPool) throws CannotCompileException, NotFoundException {
        CtClass listClass = classPool.get(List.class.getTypeName());
        CtClass jsonObjectClass = classPool.get(JsonObject.class.getTypeName());
        CtClass jsonArrayClass = classPool.get(JsonArray.class.getTypeName());
        CtClass serializableClass = classPool.get(Serializable.class.getTypeName());
        CtClass serializableArrayClass = classPool.get(Serializable[].class.getTypeName());
        CtClass intClass = classPool.get(Integer.TYPE.getTypeName());
        CtClass longClass = classPool.get(Long.TYPE.getTypeName());
        CtClass booleanClass = classPool.get(Boolean.TYPE.getTypeName());
        CtClass stringClass = classPool.get(String.class.getTypeName());

        // 查询所有
        CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{}, ctClass);
        ctClass.addMethod(selectList);

        // 根据已有键值查询
        CtMethod selectListByExample = new CtMethod(listClass, "selectListByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(selectListByExample);

        // 根据主键查询唯一对象
        CtMethod selectByPrimaryKey = new CtMethod(jsonObjectClass, "selectByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
        ctClass.addMethod(selectByPrimaryKey);

        // 查询符合条件的数据
        CtMethod selectListBy = new CtMethod(listClass, "selectListBy", new CtClass[]{stringClass}, ctClass);
        param(selectListBy, "suffixSql");
        ctClass.addMethod(selectListBy);

        // 查询特定的字段或结果
        CtMethod selectFields = new CtMethod(jsonArrayClass, "selectFields", new CtClass[]{stringClass}, ctClass);
        param(selectFields, "fields");
        ctClass.addMethod(selectFields);

        // 根据已有键值查询特定的字段或结果
        CtMethod selectFieldsByExample = new CtMethod(jsonArrayClass, "selectFieldsByExample", new CtClass[]{stringClass, jsonObjectClass}, ctClass);
        param(selectFieldsByExample, "fields", "jsonObject");
        ctClass.addMethod(selectFieldsByExample);

        // 根据主键查询特定的字段或结果
        CtMethod selectFieldsByPrimaryKey = new CtMethod(jsonArrayClass, "selectFieldsByPrimaryKey", new CtClass[]{stringClass, serializableClass}, ctClass);
        param(selectFieldsByPrimaryKey, "fields", "primaryKey");
        ctClass.addMethod(selectFieldsByPrimaryKey);

        // 根据主键查询特定的字段或结果
        CtMethod selectFieldsByPrimaryKeys = new CtMethod(jsonArrayClass, "selectFieldsByPrimaryKeys", new CtClass[]{stringClass, serializableArrayClass}, ctClass);
        param(selectFieldsByPrimaryKeys, "fields", "array");
        ctClass.addMethod(selectFieldsByPrimaryKeys);

        // 查询符合条件的特定字段或结果
        CtMethod selectFieldsBy = new CtMethod(jsonArrayClass, "selectFieldsBy", new CtClass[]{stringClass, stringClass}, ctClass);
        param(selectFieldsBy, "fields", "suffixSql");
        ctClass.addMethod(selectFieldsBy);

        // 查询符合条件的数据数量
        CtMethod countBy = new CtMethod(longClass, "countBy", new CtClass[]{stringClass}, ctClass);
        param(countBy, "suffixSql");
        ctClass.addMethod(countBy);

        // 根据已有键值查询是否存在符合条件的数据数量
        CtMethod countByExample = new CtMethod(longClass, "countByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(countByExample);

        // 是否存在符合条件的数据
        CtMethod existsBy = new CtMethod(booleanClass, "existsBy", new CtClass[]{stringClass}, ctClass);
        param(existsBy, "suffixSql");
        ctClass.addMethod(existsBy);

        // 根据已有键值查询是否存在符合条件的数据
        CtMethod existsByExample = new CtMethod(booleanClass, "existsByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(existsByExample);

        // 新增对象
        CtMethod insert = new CtMethod(intClass, "insert", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(insert);

        // 批量新增对象
        CtMethod insertBatch = new CtMethod(intClass, "insertBatch", new CtClass[]{listClass}, ctClass);
        param(insertBatch, "list");
        ctClass.addMethod(insertBatch);

        // 根据主键修改对象属性
        CtMethod updateByPrimaryKey = new CtMethod(intClass, "updateByPrimaryKey", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(updateByPrimaryKey);

        // 修改符合条件的数据
        CtMethod updateBy = new CtMethod(intClass, "updateBy", new CtClass[]{jsonObjectClass, stringClass}, ctClass);
        param(updateBy, "jsonObject", "suffixSql");
        ctClass.addMethod(updateBy);

        // 批量根据主键修改对象属性
        CtMethod updateByPrimaryKeyBatch = new CtMethod(intClass, "updateByPrimaryKeyBatch", new CtClass[]{listClass}, ctClass);
        param(updateByPrimaryKeyBatch, "list");
        ctClass.addMethod(updateByPrimaryKeyBatch);

        // 根据主键删除对象
        CtMethod deleteByPrimaryKey = new CtMethod(intClass, "deleteByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
        ctClass.addMethod(deleteByPrimaryKey);

        // 删除符合条件的数据
        CtMethod deleteBy = new CtMethod(intClass, "deleteBy", new CtClass[]{stringClass}, ctClass);
        param(deleteBy, "suffixSql");
        ctClass.addMethod(deleteBy);

        // 批量根据主键删除对象
        CtMethod deleteByPrimaryKeys = new CtMethod(intClass, "deleteByPrimaryKeys", new CtClass[]{serializableArrayClass}, ctClass);
        param(deleteByPrimaryKeys, "array");
        ctClass.addMethod(deleteByPrimaryKeys);
    }
}
