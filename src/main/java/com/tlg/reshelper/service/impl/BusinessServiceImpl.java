package com.tlg.reshelper.service.impl;

import com.nec.lib.utils.FileUtil;
import com.tlg.reshelper.dao.ds2.SimpleMapper;
import com.tlg.reshelper.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private SimpleMapper simpleMapper;

    @Value("${properties.business_service.brand_pic_path}")
    private String BrandPicPath;

    @Override
    public Map<String, String> getBrandResNames(String brandKey) {
        Object obj = globalPicRec.get(brandKey);
        Map<String, String> result = new LinkedHashMap();
        if(obj != null && obj instanceof Map)
            result.putAll((Map)obj);
        return result;
    }

    /**
     * globalPicRec的MAP结构的第一层（品牌：MAP），第二层（目录名：MAP；或 “字符串=TITLE：字符串”），第三层（目录名：图片名时间）
     * 目录结构：品牌首字/分类目录/图片；品牌首字/数字目录的标题文件Title.txt
     */
    @Override
    public void loadGlobalBrandPicRes() {
        Map picRec = new LinkedHashMap();
        List<String> folders = FileUtil.ls(BrandPicPath, true, false);
        folders.stream().forEach(x->{   //品牌
            Map classMap = new LinkedHashMap();
            Optional<String> firstTitleFileName = FileUtil.ls(x, false, true).stream().filter(y->y.equalsIgnoreCase("Title.txt")).findFirst();//数字目录的标题
            if(firstTitleFileName.isPresent()) {
                byte[] bytes = FileUtil.getFileStream(x, firstTitleFileName.get());
                if(bytes != null && bytes.length > 0) {
                    classMap.put("TITLE", new String(bytes).trim().replaceAll("\r\n","\\|"));
                }
            }
            FileUtil.ls(x, true, false).stream().forEach(y->{   //分类（目录）
                StringBuffer filesBuffer = new StringBuffer();
                FileUtil.ls(y, false, true, true).stream().forEach(z->{ //图片
                    if(filesBuffer.length() > 0) {
                        filesBuffer.append(";");
                    }
                    filesBuffer.append(z);
                });
                classMap.put(FileUtil.pathLast(y), filesBuffer.toString());
            });
            picRec.put(FileUtil.pathLast(x), classMap);
        });
        globalPicRec.clear();
        globalPicRec.putAll(picRec);
    }

}
