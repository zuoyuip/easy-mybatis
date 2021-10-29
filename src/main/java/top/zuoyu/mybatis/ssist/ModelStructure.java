package top.zuoyu.mybatis.ssist;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
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
import top.zuoyu.mybatis.data.enums.JdbcType;
import top.zuoyu.mybatis.data.model.Column;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.temp.model.BaseModel;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * 实体构建 .
 *
 * @author: zuoyu
 * @create: 2021-10-17 16:45
 */
public class ModelStructure {

    private static final CharSequence PACKAGE_SEPARATOR = ".";
    private static final CharSequence NAME_SEPARATOR = "_";
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String NULL = "null";

    public static void registerModel(@NonNull Table table) {
        ClassPool classPool = ClassPool.getDefault();
        String packageName = ClassUtils.getPackageName(BaseModel.class);
        // 创建一个空类
        CtClass ctClass = classPool.makeClass(packageName + PACKAGE_SEPARATOR + table.getTableName());
        ctClass.setModifiers(Modifier.PUBLIC);
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        registerEntity(classPool, classFile, ctClass, constPool, table);

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
    private static void registerEntity(@NonNull ClassPool classPool, ClassFile classFile, @NonNull CtClass ctClass, ConstPool constPool, @NonNull Table table) {
        try {
            // 实现Serializable接口
            CtClass serializableInterface = classPool.get(Serializable.class.getName());
            ctClass.addInterface(serializableInterface);

            // 实现Cloneable接口
            CtClass cloneableInterface = classPool.get(Cloneable.class.getName());
            ctClass.addInterface(cloneableInterface);

            // 添加serialVersionUID
            // TODO 待解决static和final
            CtClass longClass = classPool.get(Long.TYPE.getName());
            CtField serialVersionField = new CtField(longClass, "serialVersionUID", ctClass);
            serialVersionField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(serialVersionField, CtField.Initializer.constant(ThreadLocalRandom.current().nextLong()));

            // 添加无参的构造函数
            CtConstructor ctNotParamsConstructor = new CtConstructor(new CtClass[]{}, ctClass);
            ctClass.addConstructor(ctNotParamsConstructor);

        } catch (CannotCompileException | NotFoundException e) {
            throw new CustomException("registerEntity is fail!", e);
        }
    }

    /**
     * 字段部分的生成
     */
    private static void registerField(@NonNull ClassPool classPool, CtClass ctClass, ConstPool constPool, @NonNull Column column) {
        String dataType = column.getDataType();
        String tableName = column.getTableName();
        String columnName = column.getColumnName();
        boolean isNullable = YES.equalsIgnoreCase(column.getIsNullable());
        String columnDef = column.getColumnDef();
        int type = Integer.parseInt(dataType);
        try {
            // 新增字段
            CtClass typeClass = classPool.get(JdbcType.valueOf(type).getJavaType().getName());
            CtField field = new CtField(typeClass, columnName, ctClass);
            field.setModifiers(Modifier.PRIVATE);

            // 属性注解
            FieldInfo fieldInfo = field.getFieldInfo();
            AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);


            // 是否允许为NULL
            Annotation includeAnn = new Annotation("com.fasterxml.jackson.annotation.JsonInclude", constPool);
            EnumMemberValue generationTypeValue = new EnumMemberValue(constPool);
            generationTypeValue.setType("com.fasterxml.jackson.annotation.JsonInclude.Include");
            if (isNullable) {
                generationTypeValue.setValue("NON_ABSENT");
            } else {
                generationTypeValue.setValue("ALWAYS");
            }
            includeAnn.addMemberValue("value", generationTypeValue);
            fieldAttr.addAnnotation(includeAnn);

            // 统一列
            Annotation columnAnn = new Annotation("com.fasterxml.jackson.annotation.JsonProperty", constPool);
            columnAnn.addMemberValue("value", new StringMemberValue(columnName, constPool));
            if (StringUtils.hasLength(columnDef) && !JdbcType.valueOf(type).getJavaType().isAssignableFrom(Date.class)) {
                columnAnn.addMemberValue("defaultValue", new StringMemberValue(columnDef, constPool));
            }
            fieldAttr.addAnnotation(columnAnn);

            // 属性别名
            Annotation aliasAnn = new Annotation("com.fasterxml.jackson.annotation.JsonAlias", constPool);
            aliasAnn.addMemberValue("value", new StringMemberValue(tableName + NAME_SEPARATOR + columnName, constPool));
            fieldAttr.addAnnotation(aliasAnn);

            // 是否为时间类型
            if (JdbcType.valueOf(type).getJavaType().isAssignableFrom(Date.class)) {
                Annotation formatAnn = new Annotation("com.fasterxml.jackson.annotation.JsonFormat", constPool);
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
