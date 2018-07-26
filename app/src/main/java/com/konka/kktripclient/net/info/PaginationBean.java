package com.konka.kktripclient.net.info;

/**
 * Created by The_one on 2017-10-23.
 */

public class PaginationBean {
    /**
     * page : 1
     * pageSize : 3
     * count : 2
     */

    private int page;
    private int pageSize;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
