package com.lnsoft.session;

import java.util.List;

/**
 * mybatis的sqlSession是对外提供数据访问的主要API，
 * 但实际上sqlSession的功能都是基于Executor来实现的。
 * <p></p>
 * Created By Chr on 2019/1/28/0028.
 */
public interface SqlSession {

    /**
     * 根据传入的条件，查询单一结果
     *
     * @param statement 方法对应的sql语句，namespace+id
     * @param parameter 要传入到sql语句中的查询参数
     * @param <T>
     * @return 返回指定的结果对象
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 根据传入的条件，查询泛型集合
     *
     * @param statement 方法对应的sql语句，namespace+id
     * @param parameter 要传入到sql语句中的查询参数
     * @param <E>
     * @return 返回指定结果对象的list
     */
    <E> List<E> selectList(String statement, Object parameter);

    /**
     * @param type
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> type);
}
