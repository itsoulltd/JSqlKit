package com.infoworks.sql;

import com.infoworks.sql.entity.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<com.it.soul.lab.sql.Person> {
    @Override
    public com.it.soul.lab.sql.Person row(ResultSet rs, int rowNum, int columnCount) throws SQLException {
        com.it.soul.lab.sql.Person person = new com.it.soul.lab.sql.Person();
        person.setUuid_idx(rs.getString("uuid"));
        person.setAge(rs.getInt("age"));
        person.setName_test(rs.getString("name"));
        person.setSalary(rs.getDouble("salary"));
        person.setCreateDate(rs.getTimestamp("createDate"));
        person.setCreateTime(rs.getTimestamp("createTime"));
        return person;
    }
}
