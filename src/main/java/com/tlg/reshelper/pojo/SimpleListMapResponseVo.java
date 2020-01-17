package com.tlg.reshelper.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleListMapResponseVo<T> extends BaseResponseVo {

    public List<T> resultList = new ArrayList<>();

    public Map<String, Object> resultMap = new HashMap<>();
}