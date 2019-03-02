package com.lnsoft.executor;

import com.lnsoft.config.Configuration;
import com.lnsoft.config.MappedStatement;
import com.lnsoft.utils.ReflectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 第三阶段：executor的实现
 * <p>
 * Created By Chr on 2019/1/28/0028.
 */
public class DefaultExecutor implements Executor {

    //conf必须一层一层转递
    private Configuration conf;

    public DefaultExecutor(Configuration conf) {
        super();
        this.conf = conf;
    }

    //executor对数据库操作遵循什么样的规范？jdbc规范
    @Override
    public <E> List<E> query(MappedStatement mappedStatement, Object parameter) {
        List<E> ret = new ArrayList<>();//定义返回结果集
        try {
            Class.forName(conf.getJdbcDriver());//加载驱动程序
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //获得链接，从mappedStatement中获得数据库信息
            connection = DriverManager.getConnection(conf.getJdbcUrl(), conf.getJdbcUsername(), conf.getJdbcPassword());
            //创建prepareStatement，从mappedStatement获得sql语句
            preparedStatement = connection.prepareStatement(mappedStatement.getSql());
            //处理sql语句的占位符*******
            parameterize(preparedStatement, parameter);
            //执行查询语句获得resultSet（sql执行并返回查询返回的结果集）
            resultSet = preparedStatement.executeQuery();
            //将结果集通过反射技术，填充到list中*******
            handlerResultSet(resultSet, ret, mappedStatement.getResultType());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(mappedStatement.getSql());
//        System.out.println(mappedStatement.getResultType());
//        System.out.println(mappedStatement.getNamespace());
//        System.out.println(mappedStatement.getSourceId());
        return ret;
    }

    /**
     * 读取resultSet中的数据，并转换成目标对象
     *
     * @param resultSet 数据库查询sql语句运行的结果集
     * @param ret       返回的集合list
     * @param className 类的权限定名自
     * @param <E>       返回值
     */
    private <E> void handlerResultSet(ResultSet resultSet, List<E> ret, String className) {
        Class<E> clazz = null;
        try {
            //通过反射获取对象
            clazz = (Class<E>) Class.forName(className);

            while (resultSet.next()) {//遍历resultSet,如果有下一个值
                //通过反射实例化对象（resultType的对象）
                Object entity = clazz.newInstance();
                //使用反射工具将resultSet中的数据填充到entity实例中***
                ReflectionUtil.setPropToBeanFromResultSet(entity, resultSet);
                //对象加入返回集合中
                ret.add((E) entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对prepareStatement中的占位符 ? 进行处理：将参数，赋值到mapper.xml的sql语句中的占位符上
     *
     * @param preparedStatement sql语句的？占位符
     * @param parameter         所调用方法的参数
     * @throws SQLException
     */
    private void parameterize(PreparedStatement preparedStatement, Object parameter) throws SQLException {
        if (parameter instanceof Integer) {
            preparedStatement.setInt(1, (int) parameter);
        } else if (parameter instanceof Long) {
            preparedStatement.setLong(1, (long) parameter);
        } else if (parameter instanceof String) {
            preparedStatement.setString(1, (String) parameter);
        } else if (parameter instanceof Double) {
            preparedStatement.setDouble(1, (double) parameter);
        }

    }
}
