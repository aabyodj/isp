package by.aab.isp.dao.jdbc;

enum SqlDialect {

    MYSQL(
            "com.mysql.cj.jdbc.Driver",
            "SERIAL"),
    POSTGRESQL(
            "org.postgresql.Driver",
            "SERIAL8");

    private final String driverClassName;
    private final String serial8Type;

    SqlDialect(String driverClassName, String serial8Type) {
        this.driverClassName = driverClassName;
        this.serial8Type = serial8Type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getSerial8Type() {
        return serial8Type;
    }
}