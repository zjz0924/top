## OperationLog 自动生成日记记录 ##

### Service层的方法必须满足以下条件： ###

1. 无参数的方法不会自动产生记录

2. 第一个参数必须是userName, 第二个参数必须是那个实体对象

3. service 方法命名的规则
- 以 **persist、save、create、update、delete、remove** 的方法都会自动记录
- 如果其它命名开始又想自动记录日志，可以使用 **OperationLogNeeded**
- 如果不想记录可以使用 **OperationLogIgnore** 注解
   

### 配置 ###
1. **wow.operationlog.manager.ProvOperationLogAspect** 所有规定在这里设置

2. 添加新的对象，要在 **EntityServiceTypeMap** 中加上对应的类名和Dao
   要继承 JpaEntity 抽象类