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
package top.zuoyu.mybatis.ssist;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
import top.zuoyu.mybatis.exception.EasyMybatisException;
import top.zuoyu.mybatis.json.JsonArray;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.MapperRepository;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * Mapper???????????? .
 *
 * @author: zuoyu
 * @create: 2021-11-01 15:45
 */
class MapperStructure {


    @NonNull
    static Resource registerMapper(@NonNull String tableName) {
        ClassPool classPool = ClassPool.getDefault();

        // ??????????????????
        CtClass ctClass = classPool.makeInterface(Constant.MAPPER_PACKAGE_NAME + Constant.PACKAGE_SEPARATOR + String.format(Constant.MAPPER_SUFFIX, StrUtil.captureName(tableName)));
        ctClass.setModifiers(Modifier.setPublic(Modifier.INTERFACE));
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        // ????????????
        AnnotationsAttribute classAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);

        Annotation interfaceAnnMapper = new Annotation(Mapper.class.getTypeName(), constPool);
        classAttr.addAnnotation(interfaceAnnMapper);

        Annotation interfaceAnnRepository = new Annotation(Repository.class.getTypeName(), constPool);
        interfaceAnnRepository.addMemberValue("value", new StringMemberValue(tableName, constPool));
        classAttr.addAnnotation(interfaceAnnRepository);

        classFile.addAttribute(classAttr);

        try {

            CtClass unifyService = classPool.get(MapperRepository.class.getTypeName());
            ctClass.addInterface(unifyService);

            // ????????????
            methodList(ctClass, classPool);
            // ?????????class????????????
            ctClass.toClass();
            byte[] bytecode = ctClass.toBytecode();
            InputStream byteArrayInputStream = new ByteArrayInputStream(bytecode);
            return new InputStreamResource(byteArrayInputStream, tableName + "MapperInputStream");
        } catch (NotFoundException | CannotCompileException | IOException e) {
            throw new EasyMybatisException(e.getMessage(), e);
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

        // ????????????
        CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{}, ctClass);
        ctClass.addMethod(selectList);

        // ????????????????????????
        CtMethod selectListByExample = new CtMethod(listClass, "selectListByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(selectListByExample);

        // ??????????????????????????????
        CtMethod selectByPrimaryKey = new CtMethod(jsonObjectClass, "selectByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
        ctClass.addMethod(selectByPrimaryKey);

        // ???????????????????????????
        CtMethod selectListBy = new CtMethod(listClass, "selectListBy", new CtClass[]{stringClass}, ctClass);
        param(selectListBy, "suffixSql");
        ctClass.addMethod(selectListBy);

        // ??????????????????????????????
        CtMethod selectFields = new CtMethod(jsonArrayClass, "selectFields", new CtClass[]{stringClass}, ctClass);
        param(selectFields, "fields");
        ctClass.addMethod(selectFields);

        // ????????????????????????????????????????????????
        CtMethod selectFieldsByExample = new CtMethod(jsonArrayClass, "selectFieldsByExample", new CtClass[]{stringClass, jsonObjectClass}, ctClass);
        param(selectFieldsByExample, "fields", "jsonObject");
        ctClass.addMethod(selectFieldsByExample);

        // ??????????????????????????????????????????
        CtMethod selectFieldsByPrimaryKey = new CtMethod(jsonArrayClass, "selectFieldsByPrimaryKey", new CtClass[]{stringClass, serializableClass}, ctClass);
        param(selectFieldsByPrimaryKey, "fields", "primaryKey");
        ctClass.addMethod(selectFieldsByPrimaryKey);

        // ??????????????????????????????????????????
        CtMethod selectFieldsByPrimaryKeys = new CtMethod(jsonArrayClass, "selectFieldsByPrimaryKeys", new CtClass[]{stringClass, serializableArrayClass}, ctClass);
        param(selectFieldsByPrimaryKeys, "fields", "array");
        ctClass.addMethod(selectFieldsByPrimaryKeys);

        // ??????????????????????????????????????????
        CtMethod selectFieldsBy = new CtMethod(jsonArrayClass, "selectFieldsBy", new CtClass[]{stringClass, stringClass}, ctClass);
        param(selectFieldsBy, "fields", "suffixSql");
        ctClass.addMethod(selectFieldsBy);

        // ?????????????????????????????????
        CtMethod countBy = new CtMethod(longClass, "countBy", new CtClass[]{stringClass}, ctClass);
        param(countBy, "suffixSql");
        ctClass.addMethod(countBy);

        // ???????????????????????????????????????????????????????????????
        CtMethod countByExample = new CtMethod(longClass, "countByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(countByExample);

        // ?????????????????????????????????
        CtMethod existsBy = new CtMethod(booleanClass, "existsBy", new CtClass[]{stringClass}, ctClass);
        param(existsBy, "suffixSql");
        ctClass.addMethod(existsBy);

        // ?????????????????????????????????????????????????????????
        CtMethod existsByExample = new CtMethod(booleanClass, "existsByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(existsByExample);

        // ????????????
        CtMethod insert = new CtMethod(intClass, "insert", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(insert);

        // ??????????????????
        CtMethod insertBatch = new CtMethod(intClass, "insertBatch", new CtClass[]{listClass}, ctClass);
        param(insertBatch, "list");
        ctClass.addMethod(insertBatch);

        // ??????????????????????????????
        CtMethod updateByPrimaryKey = new CtMethod(intClass, "updateByPrimaryKey", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(updateByPrimaryKey);

        // ???????????????????????????
        CtMethod updateBy = new CtMethod(intClass, "updateBy", new CtClass[]{jsonObjectClass, stringClass}, ctClass);
        param(updateBy, "jsonObject", "suffixSql");
        ctClass.addMethod(updateBy);

        // ????????????????????????????????????
        CtMethod updateByPrimaryKeyBatch = new CtMethod(intClass, "updateByPrimaryKeyBatch", new CtClass[]{listClass}, ctClass);
        param(updateByPrimaryKeyBatch, "list");
        ctClass.addMethod(updateByPrimaryKeyBatch);

        // ????????????????????????
        CtMethod deleteByPrimaryKey = new CtMethod(intClass, "deleteByPrimaryKey", new CtClass[]{serializableClass}, ctClass);
        ctClass.addMethod(deleteByPrimaryKey);

        // ???????????????????????????
        CtMethod deleteBy = new CtMethod(intClass, "deleteBy", new CtClass[]{stringClass}, ctClass);
        param(deleteBy, "suffixSql");
        ctClass.addMethod(deleteBy);

        // ??????????????????????????????
        CtMethod deleteByPrimaryKeys = new CtMethod(intClass, "deleteByPrimaryKeys", new CtClass[]{serializableArrayClass}, ctClass);
        param(deleteByPrimaryKeys, "array");
        ctClass.addMethod(deleteByPrimaryKeys);

        // ??????????????????????????????
        CtMethod deleteByExample = new CtMethod(intClass, "deleteByExample", new CtClass[]{jsonObjectClass}, ctClass);
        ctClass.addMethod(deleteByExample);
    }
}
