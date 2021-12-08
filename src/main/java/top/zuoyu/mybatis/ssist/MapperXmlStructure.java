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

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.lang.NonNull;

import top.zuoyu.mybatis.data.model.Table;
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

        System.out.println(stringWriter);
        return stringWriter.toString();
    }
}
