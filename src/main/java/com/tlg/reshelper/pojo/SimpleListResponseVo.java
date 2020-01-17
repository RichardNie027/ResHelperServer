package com.tlg.reshelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class SimpleListResponseVo<T> extends BaseResponseVo {

    public List<T> result = new ArrayList<>();

}