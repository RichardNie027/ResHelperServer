package com.tlg.reshelper.dao.ds1;

import org.apache.ibatis.annotations.Select;

public interface GoodsMapper {

    //@Select("SELECT top 1 case when left(upper(talka),4)='\\\\OA' then '\\\\192.168.1.8'+substring(talka,5,1000) else '' end AS pic FROM dbo.coloth_t WHERE colthno=#{goodsNo}")
    @Select("SELECT top 1 talka AS pic FROM dbo.coloth_t WHERE colthno=#{goodsNo}")
    String selectGoodsPictureName(String goodsNo);

}
