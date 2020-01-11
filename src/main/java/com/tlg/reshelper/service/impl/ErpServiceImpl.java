package com.tlg.reshelper.service.impl;

import com.tlg.reshelper.dao.ds1.GoodsMapper;
import com.tlg.reshelper.service.ErpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErpServiceImpl implements ErpService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public String getGoodsPictureName(String goodsNo) {
        return goodsMapper.selectGoodsPictureName(goodsNo);
    }

    @Override
    public List<String> getAllGoodsNoInSameStyle(String goodsNo) {
        return goodsMapper.selectGoodsNoInSameStyle(goodsNo);
    }

}
