package com.tlg.reshelper.controller;

import com.nec.lib.utils.Base64Util;
import com.nec.lib.utils.FileUtil;
import com.nec.lib.utils.RedisUtil;
import com.tlg.reshelper.pojo.BaseResponseEntity;
import com.tlg.reshelper.service.ErpService;
import com.tlg.reshelper.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ErpService erpService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/pre_api/query_pic/{goodsNo}", method= RequestMethod.GET)
    private BaseResponseEntity queryPic(HttpServletResponse response, @PathVariable String goodsNo) {
        BaseResponseEntity result = new BaseResponseEntity();
        RedisUtil redisUtil = RedisUtil.getInstance(redisTemplate);
        if(redisUtil.hasKey(goodsNo)) {
            result.code = 200;
        } else {
            String picPathName = erpService.getGoodsPictureName(goodsNo).trim();
            int lastIdx = picPathName==null ? -1 : picPathName.lastIndexOf("\\");
            if(picPathName == null || lastIdx == -1) {
                result.code = 404;
            } else {
                byte[] bytes = FileUtil.getFileStream(picPathName.substring(0, lastIdx + 1), picPathName.substring(lastIdx + 1));
                if (bytes == null) {
                    result.code = 200;
                    redisUtil.set(goodsNo, "");
                    redisUtil.expire(goodsNo, 3600);
                } else {
                    if (redisUtil.set(goodsNo, Base64Util.byteArrayToBase64(bytes))) {
                        result.code = 200;
                        redisUtil.expire(goodsNo, 3600);
                    } else
                        result.code = 500;
                }
            }
        }
        return result;
    }

}
