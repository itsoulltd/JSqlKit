package com.infoworks.sql.query.models;

public class Or extends And {

	public Or(Expression lhr, Expression rhr) {
		super(lhr, rhr);
	}

	@Override
	public String interpret() {
		if (skipParenthesis() == false) return leftParenthesis + " " + lhr.interpret() + " OR " + rhr.interpret() + " " + rightParenthesis;
		else return lhr.interpret() + " OR " + rhr.interpret();
	}

}
