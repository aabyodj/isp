package by.aab.isp.repository;

import lombok.Data;

import java.util.List;

@Data
public class OrderOffsetLimit {
    private List<Order> orderList;
    private Long offset;
    private Integer limit;

    @Data
    public static class Order {
        private final String fieldName;
        private final boolean ascending;
    }

}
