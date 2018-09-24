# 一个商场项目实例

目的仅仅是为了找一个java项目，熟悉一下练练手而已。

### 插件的使用
#### mybatis-generator 配置
这个东西可以自动生成pojo, dao 和对应的xml文件               
pojo是放置的数据库映射关系文件，dao放置的是对映射文件的调用接口， xml是映射的实现                      

配置文件是main目录下面 generatorConfig.xml

[具体配置可以看这个文件](./src/main/resources/generatorConfig.xml)

然后直接执行mybatis-generator就可以了。  

在使用idea的时候，需要安装插件 free mybatis plugin                   

spring 整合 mybatis 请见 [applicationContext-datasource.xml](./src/main/resources/applicationContext-datasource.xml)                         

springmvc 配置 [dispatcher-servlet.xml](./src/main/webapp/WEB-INF/dispatcher-servlet.xml)

web.xml 配置 [web.xml](./src/main/webapp/WEB-INF/web.xml)
              

### 踩坑
- 有一个idea很坑的问题，如果我们使用扫苗注解的方式，会遇到idea报错，但是项目又是正常运行的。需要在setting更改这个配置： Autowiring for Bean Class


