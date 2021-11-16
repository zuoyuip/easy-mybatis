/*
 * Copyright (c) 2021, zuoyu (zuoyuip@foxmil.com).
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
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import top.zuoyu.mybatis.annotation.Model;
import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Column;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * 实体构建（此版本不需要） .
 *
 * @author: zuoyu
 * @create: 2021-10-17 16:45
 */
@Deprecated
class ModelStructure {


    static void registerModel(@NonNull Table table) {
        ClassPool classPool = ClassPool.getDefault();

        // 创建一个空类
        CtClass ctClass = classPool.makeClass(Constant.MODEL_PACKAGE_NAME + Constant.PACKAGE_SEPARATOR + StrUtil.captureName(table.getTableName()));
        ctClass.setModifiers(Modifier.PUBLIC);
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        // 类上注解
        AnnotationsAttribute classAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation modelAnn = new Annotation(Model.class.getTypeName(), constPool);
        modelAnn.addMemberValue("value", new StringMemberValue(table.getTableName(), constPool));
        classAttr.addAnnotation(modelAnn);
        classFile.addAttribute(classAttr);

        registerEntity(classPool, ctClass);

        Collection<Column> columns = table.getColumns();
        columns.forEach(column -> registerField(classPool, ctClass, constPool, column));


        URL basePath = ClassUtil.getBasePath();
        try {
            ctClass.writeFile(basePath.getPath());
        } catch (CannotCompileException | IOException e) {
            throw new CustomException("writeFile is fail!", e);
        }
    }

    /**
     * 类部分的生成
     */
    private static void registerEntity(@NonNull ClassPool classPool, @NonNull CtClass ctClass) {
        try {

            // 实现Serializable接口
            CtClass serializableInterface = classPool.get(Serializable.class.getTypeName());
            ctClass.addInterface(serializableInterface);

            // 实现Cloneable接口
            CtClass cloneableInterface = classPool.get(Cloneable.class.getTypeName());
            ctClass.addInterface(cloneableInterface);

            // 添加serialVersionUID
            CtClass longClass = classPool.get(Long.TYPE.getTypeName());
            CtField serialVersionField = new CtField(longClass, "serialVersionUID", ctClass);
            serialVersionField.setModifiers(Modifier.setPrivate(Modifier.STATIC | Modifier.FINAL));
            ctClass.addField(serialVersionField, CtField.Initializer.constant(ThreadLocalRandom.current().nextLong()));

            // 添加无参构造器会造成实例化时内存溢出

        } catch (CannotCompileException | NotFoundException e) {
            throw new CustomException("registerEntity is fail!", e);
        }
    }

    /**
     * 字段部分的生成
     */
    private static void registerField(@NonNull ClassPool classPool, CtClass ctClass, ConstPool constPool, @NonNull Column column) {
        Class<?> dataType = column.getDataType();
        String tableName = column.getTableName();
        String columnName = column.getColumnName();
        boolean isNullable = Constant.YES.equalsIgnoreCase(column.getIsNullable());
        String columnDef = column.getColumnDef();
        try {
            // 新增字段
            CtClass typeClass = classPool.get(dataType.getTypeName());
            CtField field = new CtField(typeClass, columnName, ctClass);
            field.setModifiers(Modifier.PRIVATE);

            // 属性注解
            FieldInfo fieldInfo = field.getFieldInfo();
            AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);


            // 是否允许为NULL
            Annotation includeAnn = new Annotation(JsonInclude.class.getTypeName(), constPool);
            EnumMemberValue generationTypeValue = new EnumMemberValue(constPool);
            generationTypeValue.setType(JsonInclude.Include.class.getTypeName());
            if (isNullable) {
                generationTypeValue.setValue(JsonInclude.Include.NON_NULL.name());
            } else {
                generationTypeValue.setValue(JsonInclude.Include.ALWAYS.name());
            }
            includeAnn.addMemberValue("value", generationTypeValue);
            fieldAttr.addAnnotation(includeAnn);

            // 统一列
            Annotation columnAnn = new Annotation(JsonProperty.class.getTypeName(), constPool);
            columnAnn.addMemberValue("value", new StringMemberValue(columnName, constPool));
            if (StringUtils.hasLength(columnDef) && !dataType.isAssignableFrom(Date.class)) {
                columnAnn.addMemberValue("defaultValue", new StringMemberValue(columnDef, constPool));
            }
            fieldAttr.addAnnotation(columnAnn);

            // 属性别名
            Annotation aliasAnn = new Annotation(JsonAlias.class.getTypeName(), constPool);
            aliasAnn.addMemberValue("value", new StringMemberValue(tableName + Constant.NAME_SEPARATOR + columnName, constPool));
            fieldAttr.addAnnotation(aliasAnn);

            // 是否为时间类型
            if (dataType.isAssignableFrom(Date.class)) {
                Annotation formatAnn = new Annotation(JsonFormat.class.getTypeName(), constPool);
                formatAnn.addMemberValue("pattern", new StringMemberValue("yyyy-MM-dd HH:mm:ss", constPool));
                fieldAttr.addAnnotation(formatAnn);
            }
            fieldInfo.addAttribute(fieldAttr);
            ctClass.addField(field);
            // 生成 getter、setter 方法
            ctClass.addMethod(CtNewMethod.getter("get" + StrUtil.captureName(columnName), field));
            ctClass.addMethod(CtNewMethod.setter("set" + StrUtil.captureName(columnName), field));
        } catch (NotFoundException | CannotCompileException e) {
            throw new CustomException("registerField is fail!", e);
        }
    }
}
