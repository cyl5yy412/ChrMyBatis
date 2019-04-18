package com.lnsoft.binding;

import com.lnsoft.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 第二阶段：代理阶段
 * 封装iBatis的编程模式，使用mapper接口方式开发初始化工作
 * <p>
 * Created By Chr on 2019/1/28/0028.
 * <p>
 * invocationHandler该类只负责帮助实现mapper接口的业务逻辑
 */
public class MappedProxy implements InvocationHandler {

    //请求需要转发给sqlsession
    private SqlSession session;

    public MappedProxy(SqlSession session) {
        super();
        this.session = session;
    }

    /**
     * 调用MappedProxy，时候就会调用invoke()
     * ******该方法就是实现接口的业务逻辑的：相当于mapper接口的实现类
     * @param proxy
     * @param method 就是mapper接口中的方法名
     * @param args   就是mapper接口中的方法的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //拿到接口中方法的返回参数
        Class<?> returnType = method.getReturnType();
        //如果返回参数是Collection类，是就是查询多个，注意这里不是Connections类
        if (Collection.class.isAssignableFrom(returnType)) {
            //拼接命名空间和方法名：namespace+id：包结构+类名+方法名，
            //System.out.println(method.getDeclaringClass().getName() + "." + method.getName());
            return session.selectList(method.getDeclaringClass().getName() + "." + method.getName(), args == null ? null : args[0]);
        }
        //不是Collection，就是查询单个
        //System.out.println(method.getDeclaringClass().getName() + "." + method.getName());
        return session.selectOne(method.getDeclaringClass().getName() + "." + method.getName(), args == null ? null : args[0]);
    }
}
