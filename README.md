# 一个商场项目实例

目的仅仅是为了找一个java项目，熟悉一下练练手而已。

## 用户模块
### 功能介绍
登录、用户名验证、注册、忘记密码、提交问题答案、重置密码、获取用户信息、更新用户信息、退出登录。

难点：横向越权和纵向越权、相应对象设计、用户模块数据表的设计

接口设计：

### 代码实现
#### 前台用户controller实现
在controller下面创建protal表示前台相关，在建立UserController相关类



## 分类管理模块

### 介绍
获取节点、添加节点、修改名称、获取分类ID、递归子节点ID

难点： 封装无线层级的树状数据结构、递归算法、处理复杂对象排重



## 商品管理模块
### 介绍
商品列表、商品搜索、图片上传、富文本上传、商品详情、商品下架、增加商品、更新商品

难点： 
Ftp服务的对接、SpringMvc文件上传、流读取Properties配置文件、抽象POJO、BO、VO之间的转换关系遗迹思路、joda-time快速入门、
静态模块、mybatis-pageHelper使用、MyBatis对List遍历的实现、Mybaits对where语句动态拼接的几个版本；
        
































## 其他零碎知识点
### mybatis-generator 配置
这个东西可以自动生成pojo, dao 和对应的xml文件               
pojo是放置的数据库映射关系文件，dao放置的是对映射文件的调用接口， xml是映射的实现                      

配置文件是main目录下面 generatorConfig.xml

[具体配置可以看这个文件](./src/main/resources/generatorConfig.xml)

然后直接执行mybatis-generator就可以了。  

在使用idea的时候，需要安装插件 free mybatis plugin                   

spring 整合 mybatis 请见 [applicationContext-datasource.xml](./src/main/resources/applicationContext-datasource.xml)                         

springmvc 配置 [dispatcher-servlet.xml](./src/main/webapp/WEB-INF/dispatcher-servlet.xml)

web.xml 配置 [web.xml](./src/main/webapp/WEB-INF/web.xml)


### org.apache.commons.lang3.StringUtils
这个包下面封装了一些列好用的字符串方法。建议去看看
例如：
```
StringUtils.EMPTY
StringUtils.isNotBlank();
StringUtils.isNotEmpty();       // 这个认为空字符串也是正确的
```

### org.springframework.util.CollectionUtils 和 org.apache.commons.collections.CollectionUtils;

### org.slf4j.Logger 日志





### java.util.UUID
这个包封装了一些列唯一识别码的相关类内容
比如:
```java
class Test {
    public static void main(String[] args){
        System.out.println(UUID.randomUUID().toString());     
    }
}
```

              

### 踩坑
- 有一个idea很坑的问题，如果我们使用扫苗注解的方式，会遇到idea报错，但是项目又是正常运行的。需要在setting更改这个配置： Autowiring for Bean Class






