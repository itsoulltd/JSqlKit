package com.infoworks.sql.query.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Not implements Expression {

	protected Expression lhr;
	
	public Not(Expression expression) {
		lhr = expression;
	}
	
	@Override
	public String interpret() {
		return " NOT " + lhr.interpret();
	}

	@Override
	public ExpressionProxy[] resolve() {
		List<ExpressionProxy> comps = new ArrayList<ExpressionProxy>();
		comps.addAll(Arrays.asList(lhr.resolve()));
		return comps.toArray(new ExpressionProxy[0]);
	}
}
