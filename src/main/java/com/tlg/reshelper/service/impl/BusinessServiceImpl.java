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

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private SimpleMapper simpleMapper;

    @Value("${properties.business_service.brand_pic_path}")
    private String BrandPicPath;

    @Override
    public Map<String, String> getBrandPicNames(String brandKey) {
        Object obj = globalPicRec.get(brandKey);
        Map<String, String> result = new LinkedHashMap();
        if(obj != null && obj instanceof Map)
            result.putAll((Map)obj);
        return result;
    }

    @Override
    public void loadGlobalBrandPic() {
        Map picRec = new LinkedHashMap();
        List<String> folders = FileUtil.ls(BrandPicPath, true, false);
        folders.stream().forEach(x->{
            Map classMap = new LinkedHashMap();
            FileUtil.ls(x, true, false).stream().forEach(y->{
                StringBuffer filesBuffer = new StringBuffer();
                FileUtil.ls(y, false, true, true).stream().forEach(z->{
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
