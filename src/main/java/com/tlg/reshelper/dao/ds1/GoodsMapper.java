package com.tlg.reshelper.dao.ds1;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@CacheNamespace(readWrite = false, flushInterval = 180000)
public interface GoodsMapper {

    //@Select("SELECT top 1 case when left(upper(talka),4)='\\\\OA' then '\\\\192.168.1.8'+substring(talka,5,1000) else '' end AS pic FROM dbo.coloth_t WHERE colthno=#{goodsNo}")
    @Select("SELECT isnull(max(talka),'') AS pic FROM dbo.coloth_t WHERE colthno=#{goodsNo} and talka<>'' and substring(talka,1,3)<>'pic'")
    String selectGoodsPictureName(String goodsNo);

    @Select("select colthno from coloth_t where (isnull(colthnob,'')<>'' and colthnob in (select isnull(colthnob,'') from coloth_t where colthno=#{goodsNo})) or colthno=#{goodsNo} order by colthno")
    List<String> selectGoodsNoInSameStyle(String goodsNo);

}
