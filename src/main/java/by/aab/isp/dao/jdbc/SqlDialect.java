package by.aab.isp.dao.jdbc;

enum SqlDialect {

    MYSQL(
            "com.mysql.cj.jdbc.Driver",
            "SERIAL",
            "CHARACTER SET utf8"),
    POSTGRESQL(
            "org.postgresql.Driver",
            "SERIAL8",
            "");

    private final String driverClassName;
    private final String serial8Type;
    private final String tableUtf8;

    SqlDialect(String driverClassName, String serial8Type, String tableUtf8) {
        this.driverClassName = driverClassName;
        this.serial8Type = serial8Type;
        this.tableUtf8 = tableUtf8;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getSerial8Type() {
        return serial8Type;
    }

    public String getTableUtf8() {
        return tableUtf8;
    }
}