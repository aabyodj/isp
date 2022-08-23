package by.aab.isp.service;

import lombok.Data;

@Data
public class Pagination {
    private long offset;
    private Integer pageSize;
    private Long totalItemsCount;

    public int getPageNumber() {
        return (int) (offset / pageSize);
    }

    public void setPageNumber(int pageNumber) {
        offset = (long) pageNumber * pageSize;
        if (totalItemsCount != null && offset >= totalItemsCount) {
            offset = (long) getLastPageNumber() * pageSize;
        } else if (offset < 0) {
            offset = 0;
        }
    }

    public int getLastPageNumber() {
        return totalItemsCount > 0 ? (int) ((totalItemsCount - 1)/ pageSize)
                                   : 0;
    }
}
