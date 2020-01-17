package com.tlg.reshelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class SimplePageListResponseVo<T> extends BaseResponseVo {

    public int page = 0;
    public int pageCount = 0;
    public int recordPerPage = 24;
    public int recordCount = 0;

    public List<T> result = new ArrayList<>();

    public SimplePageListResponseVo() {}

    public SimplePageListResponseVo(int page, int pageCount, int recordPerPage, int recordCount) {
        this.page = page;
        this.pageCount = pageCount;
        this.recordPerPage = recordPerPage;
        this.recordCount = recordCount;
    }

}
