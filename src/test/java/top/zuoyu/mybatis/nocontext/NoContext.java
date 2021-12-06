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
package top.zuoyu.mybatis.nocontext;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;

import top.zuoyu.mybatis.json.JsonObject;

/**
 * 无环境测试 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 16:27
 */
public class NoContext {

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
    public void testJson() {
        JsonObject jsonObject = new JsonObject(json).put("nickname", "小哞哞");
        System.out.println(jsonObject.toString());

        System.out.println(Boolean.TYPE.getTypeName());
    }

    @Test
    public void testClass() {
        JsonObject jsonObject = new JsonObject().put("num", 1).put("name", "小蜗牛").put("birthday", new Date());
        assert jsonObject != null;
        Student student = jsonObject.toClass(Student.class);
        System.out.println(student);

        System.out.println("----------------------------------");

        System.out.println(new JsonObject(student).put("name", "大蜗牛").toString());

    }

    @Test
    public void testArray() {
        JsonObject array = new JsonObject().put("array", Arrays.asList(10, 11, 12));
        System.out.println(array.getJsonArray("array"));
    }

    public static class Student{
        private Integer num;
        private String name;
        private Date birthday;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "num=" + num +
                    ", name='" + name + '\'' +
                    ", birthday=" + birthday +
                    '}';
        }
    }
}
