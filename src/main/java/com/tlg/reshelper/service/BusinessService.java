package com.tlg.reshelper.service;

import java.util.List;

public interface BusinessService {

    /**
     * 取得品牌展示的图片名集合
     * @param brandKey 品牌首字母
     * @param type 类别分组 0品牌介绍 1本季热卖搭配 2本机流行趋势 3面料知识
     * @return
     */
    List<String> getBrandPics(String brandKey, String type);

}
