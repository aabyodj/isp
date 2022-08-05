package by.aab.isp.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
    
    void close();
    
    Connection getConnection() throws SQLException;

    SqlDialect getDialect();

}
