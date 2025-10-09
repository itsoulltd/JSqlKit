package com.infoworks.sql.query.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class And implements Expression {

	protected Expression lhr;
	protected Expression rhr;

	protected char leftParenthesis = '(';
	protected char rightParenthesis = ')';

	public void setParenthesis(char left, char right){
		leftParenthesis = left;
		rightParenthesis = right;
		if (lhr != null && lhr instanceof And){
			((And)lhr).setParenthesis(left, right);
		}
		if (rhr != null && rhr instanceof And){
			((And)rhr).setParenthesis(left, right);
		}
	}

	public void disableParenthesis(){
		this.setParenthesis(' ', ' ');
	}

	public void enableParenthesis(){
		this.setParenthesis(leftParenthesis, rightParenthesis);
	}

	protected boolean skipParenthesis(){
		return Character.isWhitespace(leftParenthesis) && Character.isWhitespace(rightParenthesis);
	}
	
	public And(Expression lhr, Expression rhr) {
		this.lhr = lhr;
		this.rhr = rhr;
	}

	@Override
	public String interpret() {
		if (skipParenthesis() == false) return leftParenthesis + " " + lhr.interpret() + " AND " + rhr.interpret() + " " + rightParenthesis;
		else return lhr.interpret() + " AND " + rhr.interpret();
	}
	
	@Override
	public String toString() {
		return interpret();
	}
	
	@Override
	public ExpressionProxy[] resolve() {
		List<ExpressionProxy> comps = new ArrayList<ExpressionProxy>();
		comps.addAll(Arrays.asList(lhr.resolve()));
		comps.addAll(Arrays.asList(rhr.resolve()));
		return comps.toArray(new ExpressionProxy[0]);
	}

}
