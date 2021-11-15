package top.zuoyu.mybatis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.zuoyu.mybatis.annotation.Magic;
import top.zuoyu.mybatis.aspectj.dynamic.Mappers;
import top.zuoyu.mybatis.json.JsonObject;
import top.zuoyu.mybatis.service.UnifyService;

/**
 * 测试 .
 *
 * @author: zuoyu
 * @create: 2021-10-29 11:15
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Magic("wechatinfo")
    private UnifyService unifyService;


    @GetMapping
    public void testUnifyService() {
        UnifyService unifyService = Mappers.getUnifyService("wechatinfo");
        JsonObject jsonObject = new JsonObject().put("city", "焦作");
        unifyService.selectListByExample(jsonObject).forEach(System.out::println);
    }

}
