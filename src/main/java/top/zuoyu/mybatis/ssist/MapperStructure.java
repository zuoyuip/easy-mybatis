package top.zuoyu.mybatis.ssist;

import java.util.List;

import org.springframework.lang.NonNull;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
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

            CtClass modelClass = classPool.get(JsonObject.class.getTypeName());


            // 创建方法
            CtClass listClass = classPool.get(List.class.getTypeName());

            CtMethod selectList = new CtMethod(listClass, "selectList", new CtClass[]{}, ctClass);
            ctClass.addMethod(selectList);

            CtMethod selectListByExample = new CtMethod(listClass, "selectListByExample", new CtClass[]{modelClass}, ctClass);
            ctClass.addMethod(selectListByExample);

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
