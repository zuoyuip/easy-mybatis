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

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.UnifyService;
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

            // 批量新增对象
            CtMethod insertBatch = new CtMethod(intClass, "insertBatch", new CtClass[]{listClass}, ctClass);
            param(insertBatch, "list");
            ctClass.addMethod(insertBatch);

            // 根据主键修改对象属性
            CtMethod updateByPrimaryKey = new CtMethod(intClass, "updateByPrimaryKey", new CtClass[]{jsonObjectClass}, ctClass);
            ctClass.addMethod(updateByPrimaryKey);

            // 根据主键删除对象
            CtMethod deleteByPrimaryKey = new CtMethod(intClass, "deleteByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
            ctClass.addMethod(deleteByPrimaryKey);

            // 批量根据主键删除对象
            CtMethod deleteByPrimaryKeys = new CtMethod(intClass, "deleteByPrimaryKeys", new CtClass[]{serializableArrayClass}, ctClass);
            param(deleteByPrimaryKeys, "array");
            ctClass.addMethod(deleteByPrimaryKeys);

        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }

        URL basePath = ClassUtil.getBasePath();
        try {
            ctClass.writeFile(basePath.getPath());
        } catch (CannotCompileException | IOException e) {
            throw new CustomException("writeFile is fail!", e);
        }


        try {
            return classPool.toClass(ctClass);
        } catch (CannotCompileException e) {
            throw new CustomException("writeFile is fail!", e);
        }


    }

    private static void param(@NonNull CtMethod ctMethod, String value) {
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        ConstPool constPool = methodInfo.getConstPool();
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(Param.class.getTypeName(), constPool);
        annotation.addMemberValue("value", new StringMemberValue(value, constPool));
        Annotation[][] paramArrays = new Annotation[1][1];
        paramArrays[0][0] = annotation;
        parameterAnnotationsAttribute.setAnnotations(paramArrays);
        methodInfo.addAttribute(parameterAnnotationsAttribute);
    }
}
