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
package top.zuoyu.mybatis;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.MetaDataAccessException;

import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.json.convert.StringConvert;
import top.zuoyu.mybatis.service.MapperRepository;

@SpringBootTest
class EasyMybatisApplicationTests {

    @Magic("wechatinfo")
    private MapperRepository mapperRepository;

    String json = "{\n" +
            "  \"create_time\": \"2021-11-26T06:07:15.000+00:00\",\n" +
            "  \"deleted\": 0,\n" +
            "  \"update_time\": \"2021-11-26T06:07:15.000+00:00\",\n" +
            "  \"city\": \"北京\",\n" +
            "  \"country\": \"中国\",\n" +
            "  \"headimgurl\": \"https://cn.vuejs.org/images/logo.png\",\n" +
            "  \"language\": \"中文\",\n" +
            "  \"nickname\": \"小喵喵\",\n" +
            "  \"openid\": \"1214804270\",\n" +
            "  \"province\": \"河南\",\n" +
            "  \"sex\": \"1\",\n" +
            "  \"unionid\": \"1335282923\"\n" +
            "}";

    @Test
    void contextLoads() throws SQLException, MetaDataAccessException {
        System.out.println(mapperRepository.existsBy("where city like '%上海%'"));
    }

}
