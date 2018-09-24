# 一个商场项目实例

目的仅仅是为了找一个java项目，熟悉一下练练手而已。

### 插件的使用
#### mybatis-generator 配置
这个东西可以自动生成pojo, dao 和对应的xml文件               
pojo是放置的数据库映射关系文件，dao放置的是对映射文件的调用接口， xml是映射的实现                      

配置文件是main目录下面 generatorConfig.xml

[具体配置可以看这个文件](./src/main/resources/generatorConfig.xml)

然后直接执行mybatis-generator就可以了。                

