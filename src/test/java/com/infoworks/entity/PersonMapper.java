package com.infoworks.entity;

import com.infoworks.sql.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements EntityMapper<Person> {
    @Override
    public Person entity(ResultSet rs, int columnCount, int rowNum) throws SQLException {
        Person person = new Person();
        person.setUuid_idx(rs.getString("uuid"));
        person.setAge(rs.getInt("age"));
        person.setName_test(rs.getString("name"));
        person.setActive(rs.getBoolean("active"));
        person.setSalary(rs.getDouble("salary"));
        //person.setCreateDate(rs.getTimestamp("createDate"));
        //person.setCreateTime(rs.getTimestamp("createTime"));
        return person;
    }
}
