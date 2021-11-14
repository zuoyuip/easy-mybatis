package top.zuoyu.mybatis.ssist;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.model.Table;
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


    @NonNull
    static String registerMapperXml(@NonNull Table table) {

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);
        String templateName = VelocityUtils.getTemplate();
        Template template = Velocity.getTemplate(templateName, StandardCharsets.UTF_8.name());

        // 模板渲染
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);


        return stringWriter.toString();
    }
}
