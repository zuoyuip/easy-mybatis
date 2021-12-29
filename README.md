- [**官方网站**](https://mybatis.zuoyu.top/)

- [**API文档**](https://mybatis.zuoyu.top/doc/index.html)

`easy-mybatis`是一个对`Mybatis`的增强框架（插件）。在`Spring`集成`Mybatis`的基础上，将项目开发中对数据库的常用操作统一化。使用本框架可以很便捷的对数据库进行操作，提高开发效率，减少机械行为。

这个框架的初衷是，减少Java程序员千篇一律的数据库操作：[**主题初衷与诞生**。](https://mybatis.zuoyu.top/pages/52d5c3/)


## 特性

- **更便捷**

摒弃传统`mybatis`的`model`、`xml`、`dao`数据库操作模式，避繁就简，快速开发。

- **更高效**

采用预编译`SQL`，拒绝运行期间反射生成`SQL`，性能更高效。

- **无侵入**

只是对Mybatis-Spring的增强插件，对已有工程不做任何修改，仍可使用原生框架的功能，仅仅是简化了开发阶段对数据库的操作。

- **统一操作接口**

对数据库的所有操作共用一个接口，降低使用门槛，轻松操作数据库。

- **统一操作对象**

使用`JsonObject`为数据对象，提供一系列操作方法，方便从持久化对象组装为视图对象。

- **易上手**

整个框架只提供了一个接口、一个注解、两个对象，仅仅一行配置便可完成对数据库进行常用操作。

- ...

## 安利

- 在`mybatis-spring-boot`环境下，使用该框架（插件），可以减少传统`Mybatis`使用中对`model`、`xml`、`dao`的机械式开发。

- 所有的数据库操作均使用`MapperRepository`接口，通过注解`@Magic("xxx")`标记接口的数据表归属，即可直接使用。

- 该框架（插件）不妨碍同时使用传统`Mybatis`中`model`、`xml`、`dao`的数据库开发方式。

别犹豫了，一起上手吧——[**快速上手**](https://mybatis.zuoyu.top/pages/793dcb/)！

---

**本框架（插件）基于`mybatis-spring-boot`环境。**

## 安装

- **安装`mybatis-spring-boot`环境**

mybatis-spring-boot的Maven依赖：

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${spring-boot.version}</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis-spring-boot.version}</version>
        </dependency>
    </dependencies>
```

mybatis-spring-boot的Gradle依赖：

```groovy
    plugins {
    id 'org.springframework.boot' version '${springBootVersion}'
    id 'io.spring.dependency-management' version '${springManagementVersion}'
    id 'java'
    }

    dependencies {
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:${mybatisSpringVersion}'
    }
```


- **安装本框架（插件）**

Maven依赖引入：

```xml

<!-- https://mvnrepository.com/artifact/top.zuoyu.mybatis/easy-mybatis-spring-boot-starter -->
<dependency>
    <groupId>top.zuoyu.mybatis</groupId>
    <artifactId>easy-mybatis-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>

```

Gradle依赖引入：

```groovy

// https://mvnrepository.com/artifact/top.zuoyu.mybatis/easy-mybatis-spring-boot-starter
implementation 'top.zuoyu.mybatis:easy-mybatis-spring-boot-starter:1.0.0'

```


## 配置

1. **配置`spring-boot-jdbc`数据库**

```yaml

spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://172.0.0.1:3306/xxxx
    username: xxxx
    password: xxxx

```

关于`springBoot`的配置，这里不多赘述，更多移步[springBoot官网](https://spring.io/projects/spring-boot)。

2. **配置`easy-mybatis`支持的表名（例子）**

```yaml

easy-mybatis:
  table-names: teacher, student

```

这里的`table-names`配置，表示需要`easy-mybatis`框架支持的数据表名，多个表名使用逗号隔开。

即可使用`easy-mybatis`框架操作`teacher`和`student`两个数据表，**如果需要支持其他数据表，需要在此配置**。

3. **操作数据库（例子）**

```java

@SpringBootTest
class DemoApplicationTests {

    // 表示该接口用来操作名称为'teacher'的数据表
    @Magic("teacher")
    private MapperRepository teacherRepository;

    // 表示该接口用来操作名称为'student'的数据表
    @Magic("student")
    private MapperRepository studentRepository;


    // 查询teacher表下所有数据
    @Test
    void teacherTest() {
        teachertRepository.selectList().forEach(System.out::println);
    }

    // 查询student表下符合特定条件的数据
    @Test
    void studentTest() {
        studentRepository.selectListByExample(
          new JsonObject().put("birthday", "2009/12/12 12:12:12")
          ).forEach(System.out::println);
    }

}

```

使用`MapperRepository`接口对数据库进行操作，需要使用`@Magic("表名称")`标记该接口的数据表归属。

在本例中，`@Magic("teacher")`表示该`MapperRepository`为`"teacher"`数据表的操作接口，可以通过`teacherRepository`调用一系列方法完成对`"teacher"`数据表的操作。

关于本框架（插件）的更多信息，请前往[**官方网站**](https://mybatis.zuoyu.top/)。