package by.aab.isp.repository.datasource;

enum SqlDialect {

    MYSQL(
            "com.mysql.cj.jdbc.Driver",
            '`'),
    POSTGRESQL(
            "org.postgresql.Driver",
            '"');

    private final String driverClassName;
    private final char quoteChar;

    SqlDialect(String driverClassName, char quoteChar) {
        this.driverClassName = driverClassName;
        this.quoteChar = quoteChar;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public char getQuoteChar() {
        return quoteChar;
    }
}