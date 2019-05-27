![输入图片说明](https://images.gitee.com/uploads/images/2019/0509/115905_94a85669_67455.gif "20180703120743_1d6f0ed9365b6253b2a5e440fd104b7c_1.gif")
### 写在前面


> 虽然还带着研发团队，但是主业已经是产品5年了。前几天部门里的研发经理找我说：领导，你的那套老框架现在有点年岁太久，维护成本太高了，团队最近正好有时间想启动一套新的java开发框架，你看行么。当然是随他们去了，毕竟已经不写代码好几年了。
> 
> 想想也是，这个老框架已经是12年封装的框架一直用到现在，突然感觉老眼都湿润了，岁月不饶人，长江后浪推前浪，一代新人换旧人啊。
> 
> 想想怎么也的给他一个好的结果，算是给自己研发生涯送个终吧，然后就打算给开源了，说是开源，其实就是放在开源网上了，由于框架毕竟太老了，代码也被小伙子长时间迭代改的有点面目全非了，也没有技术支持，所以不建议大家项目中使用，可以用来学习和产考。
> 
> 这里陆续写几篇文章大概做一些对这个框架的介绍，包含一些实例。
> 
> 打算主要介绍框架里面自己封装的ORM框架（基于对象关系映射的数据库访问框架）代号“TURBO”（涡轮增压），因为他是系统的内核，所以就想用发动机来起名。后面会陆续分享框架里面对权限验证、单点登录，监控，传输等组件。

> 
> 项目已发布到码云上，地址：https://gitee.com/osjoe/turbo
> 
> 整套ORM基于springjdbc封装，利用java反射机制实现，这里有同学会问，外面已经有hibernate，MyBatis这些牛逼的框架了你还封装个毛线。

### 初衷是啥

1、受不了hibernate，MyBatis太多的xml配置，就想能不配置就不配置。这种思想也一直延续到自己做产品，简单就是美。

2、由于自己曾经是orale的dba，所以就自己控制底层拼装sql，为未来的优化留后门，不想啥都让hibernate和mybatis做了，还是透明的方式，无法在底层插入自己的优化手段。

3、想用对象查询用对象查询，想写sql写sql，想混合就混合。

4、为了适应自己的前端框架，使得mvc各层对接更加轻量级，让各种什么PO,BO,VO,DTO,POJO来回转换都见鬼去吧。

5、多数据源的无缝切换，0代码修改。首先配置要尽可能的少，第二是底层要适配各种数据库的常用语法。

6、多个数据源的支持更简答话。

7、可动态创建数据源。

8、mvc能不能不要DAO层。

9、方便对接自己写的各种分表分区查询啥的。

10、（50%的初衷）当初年少轻狂，也没女票，对代码爱的痴狂，半夜2点还在撸代码，觉得有必要写一个展示一下自己的能力。

 **下面就从实际例子来介绍一下这个ORM吧。** 

整个ORM目前已经适配数据库包括：Oracle、Mysql、SQLserver、Postgresql，可扩展支持及其数据库。



> 本文打算按以下流程来介绍，着重第三部分：
> 
> 1.数据源配置；2.对象关系配置；3.增删改查，事务操作、多数据源、动态创建数据源支撑等操作实例。

### 1、数据源配置

可以同时配置多个数据源，驱动也不用配置（底层直接根据链接查找不同数据库的驱动名），那种万年不变的老驱动，还天天配置个啥，一到配置的时候小伙子们就各种查百度。

#默认数据库配置


```
db.default.url=jdbc:mysql://localhost:3306/test

db.default.username=root

db.default.password=root

db.default.initPoolSize=5

db.default.maxPoolSize=5

db.default.schema=test

#第二个数据源（可无限多个），下面的v2代表第二个数据源的别名，可以自定义，实际操作中用到。

db.v2.url=jdbc:postgresql://10.237.33.127:5432/test

db.v2.username=test

db.v2.password=123456

db.v2.initPoolSize=5

db.v2.maxPoolSize=5
```


### 2、对象关系配置和注入；

我们内部将整个支持MVC三层的对象统一叫做VO，没有绝对的标准。采用注解(annotation)的方式，但已经尽量简化了。后面介绍，mvc各层就一个，可作为前端对象接收表单传值，也可根据annotation配置直接入库。

举个栗子。

先建个表吧！

为了标识这个框架支撑各种字段的能力，所以建了几个不同数据类型的字段。如：字符，CLOB，BLOB,时间戳啥的。


```
DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (

  `id` char(32) DEFAULT '' COMMENT '用户id',

  `name` varchar(255) DEFAULT NULL COMMENT '用户名',

  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',

  `head_sculpture` blob COMMENT '头像图片',

  `resume` longtext COMMENT '简历',

  `ts` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳'

) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```


看看vo的注解吧（简书的排版简直让人抓狂，ε=(´ο｀*))),不支持插入代码，为了好看这里及截图了，后面get和set我省略了，实际是有的）。

下图中可以看到，只要是和数据库字段名相同的属性都不用注释。不向数据库插入，但是用来接收前端表单数据的属性用@WsdNotDbColumn标注即可。


```
@WsdTable(name="T_USER")
public class UserVo extends ModuleFeatureBean{
  
    /**
     * 用户id
     */
    @WsdColumn(isId=true)
	private String id;
	/**
	 * 用户名
	 */
    private String name;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 简历
     */
    @WsdColumn(type=TYPE.CLOB)
    private String  resume;
    /**
     * 头像图片
     */
    @WsdColumn(type=TYPE.BLOB,name="head_sculpture")
    private byte[] headSculpture;
    
    /**
     * 时间戳
     */
    private Date ts;
    
    /**
     * 接收界面传递的值用于server的属性，不往数据库插入
     */
    @WsdNotDbColumn
    private String token;
```


 **这里看一下封装的annotation吧，和ORM相关的也就2个，如下：** 

1）表的注解（wsdTable）：


```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdTable {

	/**
	 * 此表在数据的schema名称
	 * @return
	 */
	 String schema()default "";
	/**
	 * 表名称
	 * @return
	 */
	String name() default "";
	/**
	 * 是否sharding 分表 需要在class-path 的名为table_sharding.xml里面查找和当前表名对应的分区配置
	 * @return
	 */
	boolean sharding() default false;
	
}
```


2）字段注解（WsdColumn关键注解）

这里面定义了不同数据库字段类型的映射关系，以方便一个别名适配各种数据库的类型。

例如NUMBER，可映射到("number","int","tinyint","decimal","float","double","bigint","numeric")，代表不同数据库相同类型的别名。


```
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsdColumn {
	/**
	 * 字段名
	 * @return
	 */
	String name() default "";
	/**
	 * 如果使用@see _DEFAULT值，将会使用Field的类型
	 * @return
	 */
	TYPE type() default TYPE._DEFAULT;	
	boolean isId() default false;
	boolean UUID() default false;
    enum TYPE{
    	_DEFAULT,
    	NUMBER(Integer.class,Long.class,Double.class,int.class,double.class,long.class),
    	TIMESTEMP(Date.class),
    	VARCHAR2(String.class),
    	BLOB(byte[].class),
    	CLOB(String.class);
    	
    	private TYPE(){
    		this.equalClazz = new Class<?>[]{};
    	}
    	private Class<?>[] equalClazz;
    	private TYPE(Class<?>... equalClazz){
    		this.equalClazz = equalClazz;
    	}
    	public Class<?>[] getequalClazz(){
    		return equalClazz;
    	}
    }
    /**
     * 数据库字段分组
     * @author 公众号：18岁fantasy
     * @2014-11-21 @下午7:53:24
     */
    public static enum DB_TYPE_GROUP{
    	NUMBER("number","int","tinyint","decimal","float","double","bigint","numeric"),
    	TIMESTAMP("timestamp","timestamp with local time zone","timestamp with time zone"),
    	DATE("date","datetime","time"),
    	STR("varchar2","varchar","char","nvarchar2","nvarchar"),
    	BLOB("blob"),
    	CLOB("clob","ntext","text","longtext");
    	private String[] dbType;
    	DB_TYPE_GROUP(String... dbType){
    		this.dbType = dbType;
    	}
		public String[] getDbType() {
			return dbType;
		}
```

### 3、配置启动扫描。

这里要说一下，框架设计了一个引擎启动的配置文件，里面会配置要扫描的VO对象的位置，多个要扫描的包用竖线隔开几个。

这样项目启动的时候就会对这些包进行扫描，并将所有扫描到的VO的元数据存入内容中或者缓存服务中。具体过程大家有兴趣可查看源码。

```

<BootParam>
	<!-- 要扫描的实体包 -->
	<modulePackage>
		<package>com.zlxd.*|core.*</package>
	</modulePackage>
	<!-- Spring初始化之前的监听器 -->
	<beforeSpringListener>
	</beforeSpringListener>
	<!-- Spring初始化之后的监听器 -->
	<afterSpringListener>
	</afterSpringListener>
	<!-- 拦截器按顺序执行 -->
	<interceptors>
	</interceptors>
</BootParam>
```


扫描的类似产考spring源码自己写的。具体类名为：ModuleParser。大家可以自己看。

### 4、各种数据操作

从曾删改查、事务操作、多数据源、非spring环境使用进行示例说明。

以下示例都可在类DaoTest里，可查看源码。
 **
已经封装数据库操作常用方法列表** 
![输入图片说明](https://images.gitee.com/uploads/images/2019/0509/112037_1ee8b192_67455.png "屏幕截图.png")

4.1插入操作
如果对于小型项目，我们一般不写DAO层。直接在service层通过CM，CM为服务获取入口类（当然服务也可以用用spring注入的方式）。

1)对象Insert

这里和常用的框架类似。

```
/**
   * 测试插入对象
   */
  @Test
  public void testInsert() {
    try {
      UserVo u = new UserVo();
      u.setId(UUIDGenerator.getUUID());
      u.setName("n_n");
      u.setEmail("email@163.com");
      u.setHeadSculpture(FileUtil.readFileToByteArray(new File("d:\\hp.png")));
      u.setResume("简历内容");
      u.setTs(new Date());
      CM.getDao().insertModule("测试插入对象", u);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
```

2)批量插入

```
  @Test
  public void testBatchInsert() throws IOException {
    try {
      List<UserVo> users = new ArrayList<UserVo>();
      for (int i = 0; i < 50; i++) {
        UserVo u = new UserVo();
        u.setId(UUIDGenerator.getUUID());
        u.setName("n_" + i);
        u.setEmail("email@" + i + ".com");
        u.setHeadSculpture(FileUtil.readFileToByteArray(new File("d:\\hp.png")));
        u.setResume("简历内容" + i);
        u.setTs(new Date());
        users.add(u);
      }
      CM.getDao().batchInsertModule("测试批量插入对象", users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

4.2查询操作
1）列表查询

```
@Test
  public void testListModule() {
    try {

      List<UserVo> users = CM.getDao().listModule("测试基于对象查询列表", UserVo.class, null);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

2）根据id查询


```
  @Test
  public void testGetById() {
    try {

      String[] idValuds = {"3f1d35b12ab544fdb73badc750e534e2"};
      UserVo user = CM.getDao().getModuleById("根据id获取对象", UserVo.class, idValuds);
      FileUtil.writeByteArrayToFile(new File("d:\\hp2.png"), user.getHeadSculpture());
      System.out.println(user);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

```

3）条件查询

所有的条件组合封装在了WhereCondition这个类中，采用链式封装，操作非常简洁。

```
  @Test
  public void listModuleByWhereCondition() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().eq("name", "n_21");
      List<UserVo> users = CM.getDao().listModule("测试查询", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```


3）多条件组合查询

WhereCondition封装了对所有sql基本语法的支撑，如：

=、!= 、>、>=、<、<= 、 like、not  like 、between and 、in 、 and、 or 、EXISTS 、NOT EXISTS、 IS NULL、 IS NOT NULL;


```
  @Test
  public void listModuleByMultWhereCondition() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where1Eq1().andLike("email", "%email%")
          .andGreaterThanOrEq("ts", "2019-5-8 11:08:43").and().isNotNull("resume");
      List<UserVo> users = CM.getDao().listModule("测试查询", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```


4）根据sql查询，返回对象列表

```
  @Test
  public void listModuleBySql() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().greaterThanOrEq("ts", "2019-5-8 14:08:43");
      List<UserVo> users =
          CM.getDao().listModule("测试查询", "select * from t_user  ", UserVo.class, condition);
      CollectionUtil.printCollection(users);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

5）分页查询

分页主要看Pager这个类，大家可以查看源码。

```
  @Test
  public void listPaginationModule() {
    try {
      WhereCondition condition = new WhereCondition();
      condition.where().greaterThanOrEq("ts", "2019-5-8 14:08:43");
      Pager pager = new Pager(0, 10);
      PagerData<UserVo> users =
          CM.getDao().getPagerModuleList("测试分页查询", UserVo.class, condition, pager);
      System.out.println("总条数：" + users.getTotal());
      CollectionUtil.printCollection(users.getRows());
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```
> 分页主要看Pager这个对象。
4.3修改操作
  根据id修改

```
  @Test
  public void testUpdateById() {
    try {
      UserVo user = new UserVo();
      user.setId("3cccc60b170848b1a53e3db9d519bd48");
      user.setName("new_name");
      
      CM.getDao().updateModuleById("测试根据id更新", user, new String[] {"headSculpture"});//可设置哪些字段不更新
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

 自动排除值为空的属性。
  `  @Test
  public void updateModuleByIdExecuteNull() {
    try {
      UserVo user = new UserVo();
      user.setId("bc17b776523241479354f6a4e6a2e26e");
      user.setName("new_name");
      
      CM.getDao().updateModuleByIdExecuteNull("测试根据id更新不包含值为空的对象", user);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }`


 手动设置哪些字段不更新
  @Test
  public void saveOrUpdateModuleById() {
    try {
      UserVo user = new UserVo();
      user.setId("bc17b776523241479354f6a4e6a2e261");
      user.setName("new_name");
      
      CM.getDao().saveOrUpdateModuleById("save or update", user, new String[] {"headSculpture"});
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
2）根据条件批量修改

```
  @Test
  public void testBatchUpdate() {
    try {
      UserVo user = new UserVo();
      user.setName("new_name");
      
      WhereCondition condition = new WhereCondition();
      condition.where().lessThan("Ts", "2019-05-08 15:47:11");
      
      CM.getDao().updateModule("测试删除", user, condition, new String[] {"headSculpture"});//可设置哪些字段不更新
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

4.5 SaveOrUpdate
根据数据库是否有着id来判断新增还是插入。

```
  @Test
  public void saveOrUpdateModuleById() {
    try {
      UserVo user = new UserVo();
      user.setId("bc17b776523241479354f6a4e6a2e261");
      user.setName("new_name");
      
      CM.getDao().saveOrUpdateModuleById("save or update", user, new String[] {"headSculpture"});
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

4.4删除操作
条件删除

```
  @Test
  public void testDelete() {
    WhereCondition condition = new WhereCondition();
    condition.where().isNotNull("ts").andBetween("ts",
        new BetweenValue("2019-5-8 11:17:51", "2019-5-8 16:17:51", true, true));
    try {
      CM.getDao().deleteModule("测试删除", UserVo.class, condition);
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```


2）根据id删除

```
  @Test
  public void testDeleteById() {
    try {
      CM.getDao().deleteModuleById("测试删除", UserVo.class,
          new Object[] {"06e773fb69404bf7b64a156bada11846"});
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

4.5事务操作
在一个事务中执行多个ddl操作。

```
  @Test
  public void doInSingleTransationCircle() {
    try {
     
      UserVo user = new UserVo();
      user.setId("bc17b776423241479354f6a4e6a2e261");
      user.setName("new_name");
      
      Dao dao =  CM.getDao();
      
      CM.getDao().doInSingleTransationCircle("事务操作", new SingleTransationCircleWithOutResult() {
        
        @Override
        public void actionInCircle() throws RuntimeException {
          try {
            dao.insertModule("插入", user);
            
            user.setEmail("email@1.1");
            WhereCondition condition = new WhereCondition();
            condition.where().lessThan("Ts", "2019-05-08 15:47:11");
            dao.updateModule("修改", user, condition);
            
            dao.deleteModuleById("删除", user);
          } catch (DaoException e) {
            throw new RuntimeException(e);
          }
        }
      });
      
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```

4.6多数据源操作
1.首先需提前在配置文件中配置多数据源信息。配置很简单，个支持多个。如下：

```
#默认数据库配置 
db.default.url=jdbc:mysql://localhost:3306/test
db.default.username=root
db.default.password=root
db.default.initPoolSize=5
db.default.maxPoolSize=5
db.default.schema=test

#其他数据源
db.pg.url=jdbc:postgresql://10.237.33.127:5432/test
db.pg.username=test
db.pg.password=123456
db.pg.initPoolSize=5
db.pg.maxPoolSize=5
```

2.在CM中定义。

```
public class CM extends CmCommon{


  private static final String PG_DBNAME = "pg";
  /**
   * 获另外的的数据源操作类
   */
  public static Dao getPGDao() {
    
    return getDao(PG_DBNAME);
    
  }
}
```

3.然后就可以直接调用了

```
  @Test
  public void multDataSource() {
    try {
     
      //默认数据源
      CM.getDao().listModule("测试基于对象查询列表", UserVo.class, null);
      //另外的数据源
      CM.getPGDao().listModule("测试基于对象查询列表", UserVo.class, null);
      
    } catch (DaoException e) {
      e.printStackTrace();
    }
  }
```


4.7动态创建数据源
主要用在通过界面添加数据源的情况，比如动态添加一个数据库对其进行定时监控，或者ETL中的数据源动态配置

```

  /**
   * 可在非spring环境下使用各种方法。常用场景如，比如要动态添加一个对数据库对其进行监控
   */
  public static void main(String[] args) throws Exception {
    
    //数据源参数
    DataSourceProperty dataSourceProperty = new DataSourceProperty();
    dataSourceProperty.setJdbcUrl("jdbc:mysql://localhost:3306/test");
    dataSourceProperty.setUsername("root");
    dataSourceProperty.setPassword("root");
    dataSourceProperty.setInitialSize(1);
    dataSourceProperty.setMaxActive(1);
    
    Dao dmDao = DaoFactory.createDao("动态数据源数据源", dataSourceProperty);
    List<Map<String, Object>> users = dmDao.listMap("查询", "select *  from t_user where ts<?", new Object[] {"2019-5-8 15:08:43"});
    CollectionUtil.printCollection(users);
  }
```

~~以上便是对本框架中ORMd的介绍。

大家可以从指定git地址上把代码pull下来，按步骤测试。


老司机再次共勉。祭奠写代码折腾框架的日子。也欢迎关注。同名公众号，18岁fantasy，欢迎关注！


微信公众号，欢迎关注。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0509/112913_98751bd0_67455.png "屏幕截图.png")
