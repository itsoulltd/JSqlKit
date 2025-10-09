package com.infoworks.sql.query;

public class SQLDistinctQuery extends SQLSelectQuery {

	public SQLDistinctQuery() {
		this.pqlBuffer = new StringBuffer("SELECT DISTINCT ");
	}

}
