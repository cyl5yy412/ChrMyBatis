package com.lnsoft.utils;

import com.lnsoft.dataobject.ItemInfo;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created By Chr on 2019/1/28/0028.
 * <p>
 * 反射工具包
 */
public class ReflectionUtil {
    /**
     * 为指定的bean的propName属性设置value值
     *
     * @param bean     目标对象
     * @param propName 对象的属性名
     * @param value    值
     */
    public static void setPropToBean(Object bean, String propName, Object value) {
        //entity的属性
        Field f;
        try {
            f = bean.getClass().getDeclaredField(propName);//获得对象指定的属性
            f.setAccessible(true);//将字段设置为可以通过反射进行访问
            f.set(bean, value);//为属性设置值
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从resultSet中读取一行数据，并填充到指定的实体bean
     *
     * @param entity    需要返回的待填充实体类bean
     * @param resultSet 从数据库加载返回的的数据结果集
     */
    public static void setPropToBeanFromResultSet(Object entity, ResultSet resultSet) throws SQLException {
        //通过反射获取需要返回对象里的的所有字段属性
        Field[] declaredFields = entity.getClass().getDeclaredFields();

        for (int x = 0; x < declaredFields.length; x++) {//遍历所有的字段，从resultSet中读取相应的数据，并填充至对象的属性中
            if (declaredFields[x].getType().getSimpleName().equals("String")) {//如果是字符串类型
                setPropToBean(entity, declaredFields[x].getName(), resultSet.getString(declaredFields[x].getName()));
            } else if (declaredFields[x].getType().getSimpleName().equals("Integer")) {//如果是int类型
                setPropToBean(entity, declaredFields[x].getName(), resultSet.getInt(declaredFields[x].getName()));
            } else if (declaredFields[x].getType().getSimpleName().equals("Long")) {//如果是int类型
                setPropToBean(entity, declaredFields[x].getName(), resultSet.getLong(declaredFields[x].getName()));
            } else if (declaredFields[x].getType().getSimpleName().equals("Double")) {
                setPropToBean(entity, declaredFields[x].getName(), resultSet.getDouble(declaredFields[x].getName()));
            }

        }
    }

    public static void main(String args[]) {
        ItemInfo itemInfo = new ItemInfo();
        ReflectionUtil.setPropToBean(itemInfo, "title", "阿三");
        System.out.println(itemInfo);
    }
}
