package by.aab.isp.dao.jdbc;

enum SqlDialect {

    MYSQL(
            "com.mysql.cj.jdbc.Driver"
    ),
    POSTGRESQL(
            "org.postgresql.Driver"
    );

    private final String driverClassName;

    SqlDialect(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

}