package com.tlg.reshelper.service.impl;

import com.tlg.reshelper.dao.ds2.SimpleMapper;
import com.tlg.reshelper.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private SimpleMapper simpleMapper;

    @Override
    public List<String> getBrandPics(String brandKey, String type) {
        List<String> result = new ArrayList<>();
        String[] arr = simpleMapper.selectBrandPics(brandKey, type).split("\\|");
        String path = arr[0];
        if(!path.endsWith("\\"))
            path = path + "\\";
        for (String s: arr[1].split(",")) {
            result.add(path + brandKey + "\\" + type + "\\" + s);
        }
        return result;
    }

}
