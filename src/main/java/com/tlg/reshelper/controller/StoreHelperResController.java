package com.tlg.reshelper.controller;

import com.tlg.reshelper.pojo.SimpleMapResponseVo;
import com.tlg.reshelper.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class StoreHelperResController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/brand/resNames/{brandKey}", method= RequestMethod.GET)
    private SimpleMapResponseVo brandResNames(HttpServletResponse response, @PathVariable String brandKey) {
        SimpleMapResponseVo result = new SimpleMapResponseVo();
        Map<String, String> map = businessService.getBrandResNames(brandKey);
        result.map.putAll(map);
        result.setSuccessfulMessage("OK");
        return result;
    }

}
