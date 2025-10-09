package com.infoworks.sql.executor;

import java.sql.SQLException;

public interface QueryTransaction extends AutoCloseable {
    void begin() throws SQLException;
    void end() throws SQLException;
    void abort() throws SQLException;
}
