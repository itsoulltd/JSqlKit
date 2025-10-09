package com.infoworks.sql;

import com.infoworks.sql.query.QueryType;
import com.infoworks.sql.query.SQLQuery;
import com.infoworks.sql.query.SQLSelectQuery;
import com.infoworks.sql.query.models.*;
import org.junit.Assert;
import org.junit.Test;

public class PredicateTest {
	
	private static String SELECT_TARGET = "SELECT * FROM Passenger WHERE  NOT ( ( name = ? AND salary > ? ) OR age = ? )";

	@Test
	public void test() {
		
		Predicate pred = new Where("name")
				.isEqualTo("sohana")
				.and("salary")
				.isGreaterThen(2000.0)
				.or(new Expression(new Property("age", 18), Operator.EQUAL))
				.not();
		//System.out.println(pred.interpret());
		
		SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT).columns()
									.from("Passenger")
									.where(pred).build();
		System.out.println(query.toString());
		
		Assert.assertEquals(PredicateTest.SELECT_TARGET, query.toString());
	}
	
	@Test
	public void testOnly() {
		
		Predicate pred = new Where("name").isEqualTo("sohana");
		
		SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT).columns()
									.from("Passenger")
									.where(pred).build();
		
		System.out.println(query.toString());
		
		ExpressionInterpreter exp = new Expression(new Property("name", "sohana"), Operator.EQUAL);
		
		SQLSelectQuery query2 = new SQLQuery.Builder(QueryType.SELECT).columns()
				.from("Passenger")
				.where(exp).build();
		
		Assert.assertEquals(query.toString(), query2.toString());
	}
	
	@Test
	public void testOld() {
		
		ExpressionInterpreter pred = new AndExpression(new Expression(new Property("name", "sohana"), Operator.EQUAL)
				, new Expression(new Property("salary", "20000.00"), Operator.GREATER_THAN));
		pred = new OrExpression(pred, new Expression(new Property("age", 18), Operator.EQUAL));
		pred = new NotExpression(pred);
		//System.out.println(pred.interpret());
		
		SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT).columns()
									.from("Passenger")
									.where(pred).build();
		System.out.println(query.toString());
		
		Assert.assertEquals(PredicateTest.SELECT_TARGET, query.toString());
	}

}
