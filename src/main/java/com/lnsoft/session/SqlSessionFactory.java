package com.lnsoft.session;

import com.lnsoft.config.Configuration;
import com.lnsoft.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Created By Chr on 2019/1/28/0028.
 * 第一阶段：初始化阶段
 *      首先需要连接数据库，就要读取数据库的信息，读取xml配置文件，读注解中的配置文件，创建配置对象，完成初始化
 *      只要工厂类其出现，必然需要把数据库信息，mapper.xml信息全部获取到，才能保证工厂生产sqlSession把数据传递下去
 * <p>
 *     该类的两个作用：
 * 1。配置文件加载到内存：加载配置文件，构造出来Factory的时候就加载配置文件
 * <p>
 * 2。工厂类生产sqlSession
 */
public class SqlSessionFactory {

    //1。配置文件加载到内存，实例化factory，将此配置信息conf拿到，再将此conf传下去到session，保证此conf全局唯一性传递
    private Configuration conf = new Configuration();
    //sqlsession对外提供服务，但是最终有executor提供执行查询服务的，遵循单一职责设计原则
    //sqlsession对外提供服务，拿到条件之后传给executor，最终由executor去数据库CRUD

    //构造方法
    public SqlSessionFactory() {
        //加载数据库文件配置信息：解析properties文件
        loadDbInfo();
        //加载mapper文件配置信息：解析mapper.xml文件
        loadMappersInfo();
    }

    //记录mapper.xml文件存放的位置
    public static final String MAPPER_CONFIG_LOCATION = "mappers";
    //记录数据库连接信息文件存放的位置
    public static final String DB_CONFIG_FILE = "db.properties";


    //加载数据库文件配置信息
    private void loadDbInfo() {
        //加载数据库信息配置文件
        InputStream dbIn = SqlSessionFactory.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            p.load(dbIn);//配置信息下写入Properties对象
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将数据库配置信息写入conf对象
        conf.setJdbcDriver(p.get("jdbc.driver").toString());
        conf.setJdbcPassword(p.get("jdbc.password").toString());
        conf.setJdbcUrl(p.get("jdbc.url").toString());
        conf.setJdbcUsername(p.get("jdbc.username").toString());
    }

    //加载制定配置文件夹下的所有mapper.xml
    private void loadMappersInfo() {
        URL resources = null;
        resources = SqlSessionFactory.class.getClassLoader().getResource(MAPPER_CONFIG_LOCATION);
        File mappers = new File(resources.getFile());//获取指定文件夹信息
        if (mappers.isDirectory()) {
            File[] files = mappers.listFiles();
            //遍历文件夹下所有的mapper.xml，解析信息后，注册到conf对象中
            for (File file : files) {
                loadMapperInfo(file);
            }
        }

    }

    //加载指定的mapper.xml文件：dom4j，从而采集到需要的信息
    private void loadMapperInfo(File file) {
        //创建saxReader对象
        SAXReader reader = new SAXReader();
        //通过read方法读取一个文件，转换成Document对象
        Document document = null;
        try {
            document = reader.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取根节点元素对象<mapper>
        Element root = document.getRootElement();
        //获取命名空间
        String namespace = root.attribute("namespace").getData().toString();
        //获取select子节点列表
        List<Element> selects = root.elements("select");
        //遍历select节点，将信息记录到MappedStatement对象，并记录到Configuration对象//中的mappedStatementMap
        for (Element element : selects) {
            MappedStatement mappedStatement = new MappedStatement();
            String id = element.attribute("id").getData().toString();//读取id属性
            String resultType = element.attribute("resultType").getData().toString();//读取resultType属性
            String sql = element.getData().toString();//读取SQL语句信息
            String sourceId = namespace + "." + id;
            //给mapperStatement属性赋值
            mappedStatement.setSourceId(sourceId);
            mappedStatement.setSql(sql);
            mappedStatement.setResultType(resultType);
            mappedStatement.setNamespace(namespace);
            //注册到configuration对象中
            conf.getMappedStatementMap().put(sourceId,mappedStatement);
        }
    }

    //2。工厂类生产sqlSession，把factory的conf传下去
    public SqlSession openSession(){
        return new DefaultSqlSession(conf);
    }
}
