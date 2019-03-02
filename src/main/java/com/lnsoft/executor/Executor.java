package com.lnsoft.executor;

import com.lnsoft.config.MappedStatement;

import java.util.List;

/**
 * Created By Chr on 2019/1/28/0028.
 * <p>
 * mybatis核心接口之一，定义了数据库操作最基本的方法，SqlSession的功能都是基于它来实现的
 */
public interface Executor {
    /**
     * 查询接口
     *
     * @param mappedStatement 封装sql语句的MappedStatement对象
     * @param parameter 传入sql的参数
     * @param <E>
     * @return  将数据转换成指定对象结果返回
     */
    <E> List<E> query(MappedStatement mappedStatement,Object parameter);
}
