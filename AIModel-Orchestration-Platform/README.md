# AIModel-Orchestration-Platform 智能AI对话聚合平台 (项目设计记录日志)

## Day 1：

#### (1)初始化项目结构

按模块拆分服务：

```bash
 AIModel-Orchestration-Platform
 ├── orchestrator-api  # 路由策略服务
 ├── op-gateway  # 网关服务
 ├── op-common  # 常用组件
 ├── Orchestrator-Service  # 统一服务层
 ├── user-service  # 用户鉴权与会话管理
 ├── model-proxy-deepseek  # DeepSeek代理服务  
 ├── model-proxy-glm4  # 智谱AI代理服务
 └── monitor  # 监控服务（后续扩展）
```

#### (2)user-service模块设计

将数据库部署在Linux服务器的Docker容器内

1.User表结构设计

```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY comment '用户id',
    username VARCHAR(50) UNIQUE comment '用户名称',
    password VARCHAR(100) comment '用户密码',
    phone varchar(11) comment '用户手机号码',
    created_at DATETIME comment '用户创建时间',
    status tinyint(1) comment '用户状态（1正常 2冻结）'
);
```

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250323183940619.png" alt="image-20250323183940619" style="zoom:67%;" />

2.User_apikey表结构设计

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250326112718582.png" alt="image-20250326112718582" style="zoom: 50%;" />

3.实现基于JWT令牌的 手机+验证码/密码 登录功能：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324202502840.png" alt="image-20250324202502840" style="zoom: 33%;" />

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324202535453.png" alt="image-20250324202535453" style="zoom: 50%;" />

##### ①测试发送验证码功能：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324203316442.png" alt="image-20250324203316442" style="zoom: 33%;" />

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324203335007.png" alt="image-20250324203335007" style="zoom:50%;" />

##### ②测试登录功能：

## Day 2

#### (1)model-proxy-glm4模块设计(智谱 AI)

搭建项目功能，实现doChat交流功能：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324202131730.png" alt="image-20250324202131730" style="zoom: 33%;" />

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324202603258.png" alt="image-20250324202603258" style="zoom: 33%;" />

测试结果：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250324202338428.png" alt="image-20250324202338428" style="zoom: 50%;" />

#### (2)model-proxy-deepseek模块设计(deepseek)

搭建项目功能，实现doChat交流功能：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250325004845153.png" alt="image-20250325004845153" style="zoom: 50%;" />

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250325004906666.png" alt="image-20250325004906666" style="zoom: 50%;" />

测试结果：

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250325005214077.png" alt="image-20250325005214077" style="zoom:33%;" />

* 备注：出现这样的响应信息是因为在AI控制台并未开启付费服务，无法正常获取到响应信息，但是可以证明请求已经正常发送到AI大模型中

## Day 3

#### (1)新增user-apikey表格，用于存放用户在各大AI大模型的apikey

```sql
create table user_apikey
(
    user_id           bigint       null comment '用户id',
    `glm4-apikey`     varchar(100) null comment '质谱AI模型的apikey',
    `deepseek-apikey` varchar(100) null comment 'deepseek AI模型的apikey',
    constraint user_apikey_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
)
    comment '用户所属apikey';
```

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250325102801787.png" alt="image-20250325102801787" style="zoom:50%;" />

封装根据用户id获取用户所属apikey的方法：

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250325105928838.png" alt="image-20250325105928838" style="zoom: 50%;" />

#### (2)Orchestrator-Service 统一服务层模块设计

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250325234139816.png" alt="image-20250325234139816" style="zoom:50%;" />

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250325234257202.png" alt="image-20250325234257202" style="zoom:50%;" />

该模块主要用于实现接收前端的对话请求，并将请求路由至指定的AI大模型，返回AI的回答

#### (3)实现基于OpenFeign的远程调用

先将前面设计好的三个微服务注册到Nacos注册中心 

<img src="C:\Users\82041\AppData\Roaming\Typora\typora-user-images\image-20250325010320815.png" alt="image-20250325010320815" style="zoom: 33%;" />

#### 完善orchestrator-api路由策略服务模块的设计

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250325234338775.png" alt="image-20250325234338775" style="zoom:50%;" />

为其他服务提供远程调用的client接口

## Day 4

#### (1)完善op-gateway网关服务模块的设计

##### ①过滤器编写

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250326112408047.png" alt="image-20250326112408047" style="zoom: 50%;" />

##### ②服务拦截器编写

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250326144058435.png" alt="image-20250326144058435" style="zoom: 50%;" />

##### ③OpenFeign拦截器编写

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250326144740731.png" alt="image-20250326144740731" style="zoom:50%;" />

#### (2)部署Sentinel

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327011840070.png" alt="image-20250327011840070" style="zoom: 33%;" />

## Day 5

#### (1)实现异步调用多个模型并聚合结果

1. 部署RabbitMQ服务器：

   账号and密码：op - 123456

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327113640975.png" alt="image-20250327113640975" style="zoom: 67%;" />

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327114018499.png" alt="image-20250327114018499" style="zoom: 50%;" />

2. 声明队列与交换机，并将他们进行绑定

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327154624428.png" alt="image-20250327154624428" style="zoom:50%;" />

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327154646207.png" alt="image-20250327154646207" style="zoom:50%;" />

<img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250327154755286.png" alt="image-20250327154755286" style="zoom:50%;" />

3. 实现异步调用多个模型并聚合结果(待完成)

   <img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250328005459289.png" alt="image-20250328005459289" style="zoom:50%;" />

   <img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250328005513319.png" alt="image-20250328005513319" style="zoom:50%;" />

   <img src="C:/Users/82041/AppData/Roaming/Typora/typora-user-images/image-20250328005553024.png" alt="image-20250328005553024" style="zoom:50%;" />