package com.lnsoft.session;

import com.lnsoft.binding.MappedProxy;
import com.lnsoft.config.Configuration;
import com.lnsoft.config.MappedStatement;
import com.lnsoft.executor.DefaultExecutor;
import com.lnsoft.executor.Executor;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 第二个过程：
 * Created By Chr on 2019/1/28/0028.
 * <p>
 * 1.对外提供服务，把请求转发给executor
 * 2.给mapper接口生成实现类（mybatis使用mapper编程方式为什么能执行sql语句）*****
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration conf;
    /**
     * sqlsession对外提供服务，但是最终有executor提供执行查询服务的，遵循单一职责设计原则
     * sqlsession对外提供服务，拿到条件之后传给executor，最终由executor去数据库CRUD
     */
    private Executor executor;

    //为什么要用构造方法呢？参数为什么是conf呢？
    //每个SqlSession都有configuration对象，并且这个conf是唯一的
    public DefaultSqlSession(Configuration conf) {
        super();
        this.conf = conf;
        //直接实例化
        executor = new DefaultExecutor(conf);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> result = this.selectList(statement, parameter);
        if (result == null || result.size() == 0) {
            return null;
        }
        if (result.size() == 1) {
            return result.get(0);
        }
        throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + result.size());
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        //sql语句信息从集合里拿
        MappedStatement mappedStatement = conf.getMappedStatementMap().get(statement);
        //传递给executor去真正的执行sql语句
        return executor.query(mappedStatement, parameter);
    }


    /**
     * 动态代理+反射：设计单一职责原则
     * 该方法为了生成返回实现类（其实是根据反射，获得接口的方法的信息，
     * 通过反射调用session执行该方法，
     * session再调用executor使用JDBC来加载驱动，获取连接，执行sql语句）
     *
     * @param type 接口类
     * @param <T>  返回的接口类，相当于接口的实现类
     * @return
     */
    //代理阶段
    @Override
    public <T> T getMapper(Class<T> type) {//该参数就是传进去的mapper类，根据这个接口类，得到信息
        //该类的invoke方法的目的就是：实现mapper接口的业务逻辑，相当于接mapper接口的实现类
        MappedProxy mappedProxy = new MappedProxy(this);//注意传递该session

        //单一指责原则：Proxy只负责生成接口的***实现类***
//        Proxy.newProxyInstance(接口的类加载器,该接口,InvocationHandler)生成指定接口的实现类
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mappedProxy);
    }
    /**
     * 注：DefaultSession调用MappedProxy，MappedProxy调用session，session调用executor~
     */
}
