package by.aab.isp;

import java.time.LocalDateTime;

public final class Const {
    private Const() {};

    public static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin";

    public static final int BANDWIDTH_UNLIMITED = Integer.MAX_VALUE;
    public static final long TRAFFIC_UNLIMITED = Long.MAX_VALUE;
    public static final LocalDateTime LDT_SINCE_AGES = LocalDateTime.of(1, 1, 1, 0, 0, 0);
    public static final LocalDateTime LDT_FOR_AGES = LocalDateTime.of(10000, 1, 1, 0, 0, 0);
}
