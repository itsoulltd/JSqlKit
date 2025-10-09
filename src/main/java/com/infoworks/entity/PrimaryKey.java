package com.infoworks.entity;

import com.infoworks.sql.query.models.DataType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface PrimaryKey {
	String name();
	DataType type() default DataType.STRING;
	boolean auto() default false;
}
