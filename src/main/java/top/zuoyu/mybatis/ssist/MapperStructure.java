package top.zuoyu.mybatis.ssist;

import java.io.IOException;
import java.net.URL;

import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.temp.mapper.BaseMapper;
import top.zuoyu.mybatis.temp.model.BaseModel;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * Mapper接口构建 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 15:45
 */
public class MapperStructure {

    private static final CharSequence PACKAGE_SEPARATOR = ".";
    private static final CharSequence NAME_SEPARATOR = "_";
    private static final String PACKAGE_NAME = ClassUtils.getPackageName(BaseMapper.class);
    private static final String MODEL_PACKAGE_NAME = ClassUtils.getPackageName(BaseModel.class);

    public static void registerMapper(@NonNull Table table) {
        ClassPool classPool = ClassPool.getDefault();

        // 创建一个接口
        CtClass ctClass = classPool.makeInterface(PACKAGE_NAME + PACKAGE_SEPARATOR + String.format("%sMapper", StrUtil.captureName(table.getTableName())));
        ctClass.setModifiers(Modifier.PUBLIC);
        ctClass.setModifiers(Modifier.INTERFACE);
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();


        String modelName = MODEL_PACKAGE_NAME + PACKAGE_SEPARATOR + StrUtil.captureName(table.getTableName());
        try {
            CtClass modelClass = classPool.get(modelName);


            // 类上注解
            AnnotationsAttribute classAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation repositoryAnn = new Annotation("org.springframework.stereotype.Repository", constPool);
            classAttr.addAnnotation(repositoryAnn);
            classFile.addAttribute(classAttr);

            // 创建方法
            CtClass listClass = classPool.get("java.util.List");
            CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{modelClass}, ctClass);
            ctClass.addMethod(selectList);
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }

        URL basePath = ClassUtil.getBasePath();
        try {
            ctClass.writeFile(basePath.getPath());
        } catch (CannotCompileException | IOException e) {
            throw new CustomException("writeFile is fail!", e);
        }


    }
}
