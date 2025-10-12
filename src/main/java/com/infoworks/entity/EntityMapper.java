package com.infoworks.entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author towhid
 * @since 19-Aug-19
 */
public interface EntityMapper<R extends Entity> {
    default List<R> map(ResultSet rs) throws SQLException {
        if (rs == null) return new ArrayList<>();
        List<R> collection = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        int rowIdx = 0;
        while (rs.next()){
            try {
                R entity = entity(rs, columnCount, rowIdx++);
                collection.add(entity);
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        return collection;
    }
    R entity(ResultSet rs, int columnCount, int rowIdx) throws SQLException;
}
