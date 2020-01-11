package com.tlg.reshelper.service;

import java.util.List;

public interface ErpService {

    String getGoodsPictureName(String goodsNo);

    List<String> getAllGoodsNoInSameStyle(String goodsNo);
}
