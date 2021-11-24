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
package top.zuoyu.mybatis.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.zuoyu.mybatis.annotation.Magic;
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
    public ResponseEntity<List<JsonObject>> selectList() {
        return ResponseEntity.ok(unifyService.selectList());
    }

    @GetMapping("/selectListByExample")
    public ResponseEntity<List<JsonObject>> selectListByExample(JsonObject jsonObject) {
        return ResponseEntity.ok(unifyService.selectListByExample(jsonObject));
    }

    @GetMapping("/{primaryKey}")
    public ResponseEntity<JsonObject> selectByPrimaryKey(@PathVariable Integer primaryKey) {
        return ResponseEntity.ok(unifyService.selectByPrimaryKey(primaryKey));
    }

    @PostMapping
    public ResponseEntity<Integer> insert(@RequestBody JsonObject jsonObject) {
        return ResponseEntity.ok(unifyService.insert(jsonObject));
    }

    @PutMapping
    public ResponseEntity<Integer> updateByPrimaryKey(@RequestBody JsonObject jsonObject) {
        return ResponseEntity.ok(unifyService.updateByPrimaryKey(jsonObject));
    }

    @DeleteMapping("/{primaryKey}")
    public ResponseEntity<Integer> deleteByPrimaryKey(@PathVariable Integer primaryKey) {
        return ResponseEntity.ok(unifyService.deleteByPrimaryKey(primaryKey));
    }

    @DeleteMapping("/deleteByPrimaryKeys/{primaryKeys}")
    public ResponseEntity<Integer> deleteByPrimaryKeys(@PathVariable Integer[] primaryKeys) {
        return ResponseEntity.ok(unifyService.deleteByPrimaryKeys(primaryKeys));
    }
}
