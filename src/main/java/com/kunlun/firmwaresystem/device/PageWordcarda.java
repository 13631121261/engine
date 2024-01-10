package com.kunlun.firmwaresystem.device;

import com.kunlun.firmwaresystem.entity.Wordcard_a;

import java.util.List;

public class PageWordcarda {
    List<Wordcard_a> wordcard_as;
    long page;
    long total;

    public PageWordcarda(List<Wordcard_a> wordCardAs,
                         long page,
                         long total) {
        this.wordcard_as = wordCardAs;
        this.page = page;
        this.total = total;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Wordcard_a> getWordcard_as() {
        return wordcard_as;
    }

    public void setWordcard_as(List<Wordcard_a> wordcard_as) {
        this.wordcard_as = wordcard_as;
    }

}