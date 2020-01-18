package com.tlg.reshelper.service;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BusinessService {

    static Map globalPicRec = new LinkedHashMap();

    /**
     * 取得品牌的展示资源：区分资源类别、多个图片文件的名称和时间
     * @param brandKey 品牌首字母
     * @return KEY(资源类别):0品牌介绍 1本季热卖搭配 2本机流行趋势 3面料知识 其它非自然数 VALUE(文件名|时间;*.jpg|yyyyMMddHHmm)
     */
    Map<String, String> getBrandResNames(String brandKey);
    //List<String> getBrandPics(String brandKey, String type);

    void loadGlobalBrandPicRes();
}
