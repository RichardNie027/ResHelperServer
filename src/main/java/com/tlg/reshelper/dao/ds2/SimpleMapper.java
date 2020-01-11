package com.tlg.reshelper.dao.ds2;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@CacheNamespace(readWrite = false, flushInterval = 180000)
public interface SimpleMapper {

    @Select("SELECT top 1 picBasePath + '|' + pics FROM [StoreHelper].[dbo].[H5Content] WHERE brandKey=#{brandKey} and type=#{type}")
    String selectBrandPics(String brandKey, String type);

}
