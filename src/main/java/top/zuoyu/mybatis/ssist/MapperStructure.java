package top.zuoyu.mybatis.ssist;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

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
import javassist.bytecode.annotation.StringMemberValue;
import top.zuoyu.mybatis.annotation.Model;
import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;

/**
 * Mapper接口构建 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 15:45
 */
class MapperStructure {


    static void registerMapper(@NonNull Table table) {
        ClassPool classPool = ClassPool.getDefault();

        // 创建一个接口
        CtClass ctClass = classPool.makeInterface(Constant.MAPPER_PACKAGE_NAME + Constant.PACKAGE_SEPARATOR + String.format(Constant.MAPPER_SUFFIX, StrUtil.captureName(table.getTableName())));
        ctClass.setModifiers(Modifier.setPublic(Modifier.INTERFACE));
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constPool = classFile.getConstPool();


//        String modelName = Constant.MODEL_PACKAGE_NAME + Constant.PACKAGE_SEPARATOR + StrUtil.captureName(table.getTableName());
        String modelName = JsonObject.class.getTypeName();
        try {
            CtClass modelClass = classPool.get(modelName);


            // 类上注解
            AnnotationsAttribute classAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation modelAnn = new Annotation(Model.class.getTypeName(), constPool);
            modelAnn.addMemberValue("value", new StringMemberValue(table.getTableName(), constPool));
            classAttr.addAnnotation(modelAnn);
            classFile.addAttribute(classAttr);

            // 创建方法
            CtClass listClass = classPool.get(List.class.getTypeName());
            CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{modelClass}, ctClass);
            ctClass.addMethod(selectList);
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }



//        URL basePath = ClassUtil.getBasePath();
        try {
            classPool.toClass(ctClass);
//            ctClass.writeFile(basePath.getPath());
        } catch (CannotCompileException e) {
            throw new CustomException("writeFile is fail!", e);
        }


    }
}
