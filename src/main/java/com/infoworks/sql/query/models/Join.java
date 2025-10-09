package com.infoworks.sql.query.models;

public class Join implements Expression {
	
	private String leftColumn;
	private String rightColumn;
	private String leftTable;
	private String rightTable;
	
	public Join(String leftColumn, String rightColumn) {
		this.leftColumn = leftColumn;
		this.rightColumn = rightColumn;
	}

	@Override
	public String interpret() {
		return (" ON " + leftTable  + "." +  leftColumn + " = " + rightTable + "." + rightColumn);
	}

	@Override
	public ExpressionProxy[] resolve() {
		return null;
	}

	public void setLeftTable(String leftTable) {
		this.leftTable = leftTable;
	}

	public void setRightTable(String rightTable) {
		this.rightTable = rightTable;
	}

}
