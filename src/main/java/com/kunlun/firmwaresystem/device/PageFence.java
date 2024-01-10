package com.kunlun.firmwaresystem.device;
import com.kunlun.firmwaresystem.entity.Fence;
import java.util.List;
public class PageFence {
    List<Fence> fenceList;
    long page;
    long total;

    public PageFence(List<Fence> fenceList,
                     long page,
                     long total) {
        this.fenceList = fenceList;
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

    public List<Fence> getFenceList() {
        return fenceList;
    }

    public void setFenceList(List<Fence> fenceList) {
        this.fenceList = fenceList;
    }

}