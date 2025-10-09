package com.infoworks.sql;

import com.infoworks.entity.Entity;
import com.infoworks.objects.Ignore;
import com.infoworks.entity.PrimaryKey;
import com.infoworks.entity.TableName;
import com.infoworks.sql.executor.SQLExecutor;
import com.infoworks.orm.Property;

import javax.persistence.Column;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@TableName(value = "Passenger", acceptAll = false)
public class Passenger extends Entity {
	
	@PrimaryKey(name="ID", auto =true)
	private Integer id;
	@Column(name = "AGE")
	private Integer age;
	@Column(name = "NAME")
	private String name;
	@Column(name = "SEX")
	private String sex;
	@Column(name = "DOB")
	private Date dob;
	@Column(name = "CREATEDATE")
	private Date createdate;
	@Ignore
	private static Integer _autoIncrement = 0;
	public Passenger() {}
	public Passenger(String name
			, String sex
			, int age) {
		setId(_autoIncrement++);
		this.name = name;
		this.sex = sex;
		this.age = age;
		updateDOB(age, false);
	}
	private void updateDOB(int age, boolean isPositive) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
		int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
		calendar.set(Calendar.YEAR, year);
		setDob(new java.sql.Date(calendar.getTime().getTime()));
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Property getPropertyTest(String key, SQLExecutor exe, boolean skipPrimary) {
		return getProperty(key, exe, skipPrimary);
	}

}
