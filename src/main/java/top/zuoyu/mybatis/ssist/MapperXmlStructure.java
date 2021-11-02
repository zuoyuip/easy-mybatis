package top.zuoyu.mybatis.ssist;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.common.Constant;
import top.zuoyu.mybatis.data.model.Table;
import top.zuoyu.mybatis.exception.CustomException;
import top.zuoyu.mybatis.utils.ClassUtil;
import top.zuoyu.mybatis.utils.StrUtil;
import top.zuoyu.mybatis.utils.VelocityInitializer;
import top.zuoyu.mybatis.utils.VelocityUtils;

/**
 * mapper的xml构建 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 14:25
 */
class MapperXmlStructure {


    static void registerMapperXml(@NonNull Table table) {

        String fileName = StrUtil.captureName(table.getTableName());
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);
        String templateName = VelocityUtils.getTemplate();
        Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());

        // 模板渲染
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        URL basePath = ClassUtil.getBasePath();
        File path = new File(basePath.getPath(), Constant.MAPPER_XML_DIR_NAME + File.separator + String.format(Constant.MAPPER_XML_SUFFIX, fileName));
        try {
            FileUtils.writeStringToFile(path, stringWriter.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(String.format("渲染%sMapper.xml失败", fileName));
        }
    }
}
