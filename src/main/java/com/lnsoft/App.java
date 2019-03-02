package com.lnsoft;

import com.lnsoft.dataobject.ItemInfo;
import com.lnsoft.mapper.ItemMapper;
import com.lnsoft.session.SqlSession;
import com.lnsoft.session.SqlSessionFactory;

import java.util.List;

/**
 * Hello world!
 * <p>
 * mybatis有两种编程方式：
 * ibatis方式：Apache
 * 这种方式是怎么执行的sql
 * mapper接口方式：Google
 * 这种方式只有mapper接口没有实现类，到底是怎么执行的sql
 */
public class App {
    public static void main(String[] args) {
        //System.out.println("Hello World!");
        SqlSessionFactory factory = new SqlSessionFactory();//1
        SqlSession session = factory.openSession();//1
        //System.out.println(session+"=1=");

        ItemMapper itemMapper = session.getMapper(ItemMapper.class);//1
        //System.out.println(itemMapper+"=2=");
        ItemInfo itemInfo = itemMapper.selectByPrimaryKey(1);//1
        System.out.println(itemInfo + "=3=");

        List<ItemInfo> itemInfos = itemMapper.selectAll();
        for (ItemInfo info : itemInfos
                ) {
            System.out.println(info);
        }

        //简化的核心的的mybatis
        //还有修改，新增，map中读数据，游标中读数据
//        org.apache.ibatis.session.SqlSession
    }
}
