package com.tlg.reshelper.controller;

import com.nec.lib.utils.Base64Util;
import com.nec.lib.utils.FileUtil;
import com.nec.lib.utils.RedisUtil;
import com.tlg.reshelper.pojo.BaseResponseEntity;
import com.tlg.reshelper.service.BusinessService;
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
import java.util.List;

@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ErpService erpService;
    @Autowired
    private BusinessService businessService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**内调用函数
     * @param requestGoodsNo 请求图片的货号
     * @param  asGoodsNo 以此货号保存到Redis
     */
    private BaseResponseEntity getGoodsPicResponse(String requestGoodsNo, String asGoodsNo) {
        BaseResponseEntity result = new BaseResponseEntity();
        RedisUtil redisUtil = RedisUtil.getInstance(redisTemplate);
        if(redisUtil.hasKey(asGoodsNo)) {
            result.code = 200;
        } else {
            byte[] bytes = getGoodsPicBytes(requestGoodsNo);
            if (bytes==null || bytes.length==0) {
                result.code = 404;
            } else {
                if (redisUtil.set(asGoodsNo, Base64Util.byteArrayToBase64(bytes))) {
                    result.code = 200;
                    redisUtil.expire(asGoodsNo, 3600);
                } else
                    result.code = 404;
            }
        }
        return result;
    }

    /**内调用函数*/
    private byte[] getGoodsPicBytes(String goodsNo) {
        String picPathName = erpService.getGoodsPictureName(goodsNo).trim();
        int lastIdx = picPathName==null ? -1 : picPathName.lastIndexOf("\\");
        if(picPathName == null || lastIdx == -1)
            return null;
        byte[] bytes = FileUtil.getFileStream(picPathName.substring(0, lastIdx + 1), picPathName.substring(lastIdx + 1));
        return bytes;
    }

    @RequestMapping(value = "/goods/pic/{goodsNo}", method= RequestMethod.GET)
    private BaseResponseEntity goodsPic(HttpServletResponse response, @PathVariable String goodsNo) {
        String asGoodsNo = goodsNo.toUpperCase();
        BaseResponseEntity result = getGoodsPicResponse(goodsNo, asGoodsNo);
        if(result.code == 200) {
            return result;
        } else {
            List<String> goodsNoList = erpService.getAllGoodsNoInSameStyle(goodsNo);
            for(int i=0; i<goodsNoList.size(); i++) {
                if(goodsNoList.get(i).toUpperCase().equals(goodsNo))
                    continue;
                result = getGoodsPicResponse(goodsNoList.get(i), asGoodsNo);
                if(result.code == 200) {
                    return result;
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/brand/pic/{brandKey}/{type}/{idx}", method= RequestMethod.GET)
    private BaseResponseEntity brandPic(HttpServletResponse response,  @PathVariable String brandKey, @PathVariable String type, @PathVariable int idx) {
        BaseResponseEntity result = new BaseResponseEntity();
        RedisUtil redisUtil = RedisUtil.getInstance(redisTemplate);
        String key = brandKey + "_" + type + "_" + idx;
        if(redisUtil.hasKey(key)) {
            result.code = 200;
        } else {
            List<String> picList = businessService.getBrandPics(brandKey, type);
            if(picList.size() < idx+1) {
                result.code = 404;
            } else {
                int lastIdx = picList.get(idx).lastIndexOf("\\");
                byte[] bytes = FileUtil.getFileStream(picList.get(idx).substring(0, lastIdx + 1), picList.get(idx).substring(lastIdx + 1));
                if (bytes == null) {
                    result.code = 200;
                    redisUtil.set(key, "");
                    redisUtil.expire(key, 3600);
                } else {
                    if (redisUtil.set(key, Base64Util.byteArrayToBase64(bytes))) {
                        result.code = 200;
                        redisUtil.expire(key, 3600);
                    } else
                        result.code = 500;
                }
            }
        }
        return result;
    }

}
