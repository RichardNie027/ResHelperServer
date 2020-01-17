package com.tlg.reshelper.controller;

import com.nec.lib.utils.Base64Util;
import com.nec.lib.utils.FileUtil;
import com.nec.lib.utils.RedisUtil;
import com.tlg.reshelper.pojo.BaseResponseVo;
import com.tlg.reshelper.service.BusinessService;
import com.tlg.reshelper.service.ErpService;
import com.tlg.reshelper.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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

    @Value("${properties.business_service.brand_pic_path}")
    private String BrandPicPath;

    /**内调用函数
     * @param requestGoodsNo 请求图片的货号
     * @param  asGoodsNo 以此货号保存到Redis
     */
    private BaseResponseVo getGoodsPicResponse(String requestGoodsNo, String asGoodsNo) {
        BaseResponseVo result = new BaseResponseVo();
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
    private BaseResponseVo goodsPic(HttpServletResponse response, @PathVariable String goodsNo) {
        String asGoodsNo = goodsNo.toUpperCase();
        BaseResponseVo result = getGoodsPicResponse(goodsNo, asGoodsNo);
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

    /**
     * 查询品牌图片资源，返回成功失败，成功则将图片流写进Redis
     * @param response
     * @param brandKey 品牌首字
     * @param type Base64编码的类别（目录）
     * @param name_time Base64编码的资源（文件名|时间）
     * @return 成功失败JSON
     */
    @RequestMapping(value = "/brand/pic/{brandKey}/{type}/{name_time}", method= RequestMethod.GET)
    private BaseResponseVo brandPic(HttpServletResponse response, @PathVariable String brandKey, @PathVariable String type, @PathVariable String name_time) {
        BaseResponseVo result = new BaseResponseVo();
        RedisUtil redisUtil = RedisUtil.getInstance(redisTemplate);
        String key = brandKey + "_" + type + "_" + name_time;
        if(redisUtil.hasKey(key)) {
            result.code = 200;
        } else {
            String typeDecode = Base64Util.decode(type);
            String nameDecode = Base64Util.decode(name_time);   //name & time
            if(nameDecode != null && !nameDecode.isEmpty()) {
                String[] arr = nameDecode.split("\\|");
                if(arr.length > 1)
                    nameDecode = arr[0];
                else
                    nameDecode = "";
            } else
                nameDecode = "";
            if(nameDecode.isEmpty() || typeDecode==null || typeDecode.isEmpty()) {
                result.code = 404;
            } else {
                byte[] bytes = FileUtil.getFileStream(BrandPicPath + brandKey + "\\" + typeDecode + "\\", nameDecode);
                if (bytes == null) {
                    result.code = 404;
                } else {
                    if (redisUtil.set(key, Base64Util.byteArrayToBase64(bytes))) {
                        result.code = 200;
                        redisUtil.expire(key, 3600);
                    } else
                        result.code = 404;
                }
            }
        }
        return result;
    }

}
