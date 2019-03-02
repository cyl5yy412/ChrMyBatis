package com.lnsoft.mapper;

import com.lnsoft.dataobject.ItemInfo;

import java.util.List;

/**
 * Created By Chr on 2019/1/28/0028.
 */
public interface ItemMapper {

    ItemInfo selectByPrimaryKey(Integer id);

    List<ItemInfo> selectAll();
}
