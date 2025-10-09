package com.infoworks.sql.query;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.orm.Row;
import com.infoworks.sql.query.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLSelectQuery extends SQLQuery{
	
	protected StringBuffer pqlBuffer;
	protected Integer limit = 0;
	protected Integer offset = 0;
	protected List<String> orderByList;
	protected List<String> groupByList;
	protected Expression havingInterpreter;
	private char quantifier = ' '; //Default is empty space
	
	public SQLSelectQuery() {
		this.pqlBuffer = new StringBuffer("SELECT ");
	}
	
	@Override
	protected String queryString(JDBCDriverClass dialect) throws IllegalArgumentException {
		super.queryString(dialect);
		appendLimit(pqlBuffer, dialect);
		return pqlBuffer.toString();
	}
	
	protected SQLSelectQuery setQuantifier(char quantifier){
		this.quantifier = quantifier;
		return this;
	}
	
	protected char getQuantifier() {
		return quantifier;
	}
	
	protected Boolean quantifierEnabled(){
		return Character.isWhitespace(quantifier) == false;
	}

	public void setLimit(Integer limit, Integer offset) {
		this.limit = (limit < 0) ? 0 : limit;
		this.offset = (offset < 0) ? 0 : offset;
	}

	protected void appendLimit(StringBuffer pqlBuffer, JDBCDriverClass dialect) {
		//General ANIS-SQL Format => OFFSET %s ROWS FETCH NEXT %s ROWS ONLY
		//Mysql & H2 variants SQL Format => LIMIT %s OFFSET %s
		if (limit > 0) {
			if (dialect == JDBCDriverClass.MYSQL
					|| dialect == JDBCDriverClass.H2_EMBEDDED
					|| dialect == JDBCDriverClass.H2_FILE
					|| dialect == JDBCDriverClass.H2_SERVER
					|| dialect == JDBCDriverClass.H2_SERVER_TLS
					|| dialect == JDBCDriverClass.HSQL_EMBEDDED){
				//
				if (pqlBuffer.toString().contains("LIMIT")) return;
				pqlBuffer.append(" LIMIT " + limit) ;
				if (offset >= 0) { pqlBuffer.append(" OFFSET " + offset) ;}
			}else {
				//
				if (pqlBuffer.toString().contains("LIMIT")) {
					String limitStr = " LIMIT " + limit;
					int start = pqlBuffer.toString().indexOf(limitStr);
					int end = pqlBuffer.length();
					pqlBuffer.replace(start, end, "");
				}
				if (pqlBuffer.toString().contains("ROWS FETCH NEXT")) return;
				pqlBuffer.append(String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", offset, limit));
			}
		}
	}

	public Integer getLimit(){
	    return this.limit;
    }

    public Integer getOffset() {
        return offset;
    }

    @SuppressWarnings("Duplicates")
	public void setOrderBy(List<String> columns, Operator opt) {
		this.orderByList = columns;
		if (columns != null && columns.size() > 0) {
			StringBuffer orderBuffer = new StringBuffer(" ORDER BY ");
			int count = 0;
			for(String col : columns) {
				if(col.trim().equals("")){continue;}
				if(count++ != 0){orderBuffer.append(", ");}
				if(quantifierEnabled()) {orderBuffer.append(getQuantifier() + "." + col);}
				else {orderBuffer.append(col);}
				if (opt != null) {orderBuffer.append(" " + opt.toString());}
			}
			if (count > 0) {
				pqlBuffer.append(orderBuffer.toString());
			}
		}
	}

	@SuppressWarnings("Duplicates")
	public void setGroupBy(List<String> columns) {
		this.groupByList = columns;
		if (columns != null && columns.size() > 0) {
			StringBuffer groupBuffer = new StringBuffer(" GROUP BY ");
			int count = 0;
			for(String col : columns) {
				if(col.trim().equals("")){continue;}
				if(count++ != 0){groupBuffer.append(", ");}
				if(quantifierEnabled()){groupBuffer.append(getQuantifier() + "." + col);}
				else {groupBuffer.append(col);}
			}
			if (count > 0) {
				pqlBuffer.append(groupBuffer.toString());
			}
		}
	}
	
	public void setHavingExpression(Expression interpreter) {
		this.havingInterpreter = interpreter;
		pqlBuffer.append(" HAVING " + interpreter.interpret());
	}
	
	@Override
	public Row getWhereProperties() {
		if(havingInterpreter != null) {
			List<ExpressionProxy> expressions = (super.getWhereParamExpressions() != null)
					? new ArrayList<>(super.getWhereParamExpressions())
					: new ArrayList<>();
			expressions.addAll(Arrays.asList(havingInterpreter.resolve()));
			super.setWhereParamExpressions(expressions);
		}
		return super.getWhereProperties();
	}
	
	@Override
	public void setColumns(String[] columns) {
		super.setColumns(columns);
		prepareColumns(getColumns());
	}
	
	protected void prepareColumns(String[] columns){
		if(getColumns() != null && getColumns().length > 0){
			int count = 0;
			for(String str : getColumns()){
				if(str.trim().equals("")){continue;}
				if(count++ != 0){pqlBuffer.append(", ");}
				pqlBuffer.append(str);
			}
			//If all passed parameter is empty
			if(count == 0){pqlBuffer.append(STARIC);}
		}else{
			pqlBuffer.append(STARIC);
		}
	}
	
	@Override
	public void setTableName(String tableName) {
		super.setTableName(tableName);
		prepareTableName(tableName);
	}
	
	protected void prepareTableName(String name){
		pqlBuffer.append(" FROM "+ name + " ");
	}
	
	@Override
	public void setWhereParams(String[] whereParams) {
		super.setWhereParams(whereParams);
		prepareWhereParams(whereParams);
	}
	
	protected void prepareWhereParams(String[] whereParams) {
		prepareWhereParams(ExpressionProxy.createListFrom(whereParams, Operator.EQUAL));
	}
	
	@Override
	public void setWhereParamExpressions(List<ExpressionProxy> whereParams) {
		super.setWhereParamExpressions(whereParams);
		prepareWhereParams(whereParams);
	}

	@SuppressWarnings("Duplicates")
	protected void prepareWhereParams(List<ExpressionProxy> whereParams) {
		if(whereParams != null 
				&& whereParams.size() > 0
				&& !isAllParamEmpty(whereParams.toArray())){
			
			if(pqlBuffer.length() > 0){
				pqlBuffer.append("WHERE ");
				int count = 0;
				for(ExpressionProxy param : whereParams){
					if(param.getProperty().trim().equals("")){continue;}
					if(count++ != 0){pqlBuffer.append( " " + getLogic().name() + " ");}
					pqlBuffer.append(param.getProperty() + " " + param.getType().toString() + " " + MARKER);
				}
			}
		}
	}
	
	@Override
	public void setWhereExpression(Expression whereExpression) {
		super.setWhereExpression(whereExpression);
		prepareWhereExpression(whereExpression);
	}
	
	protected void prepareWhereExpression(Expression whereExpression){
		pqlBuffer.append("WHERE " + whereExpression.interpret());
	}

	@Deprecated
    @SuppressWarnings("Duplicates")
	public static String create(String tableName, String[]projectionParams, Logic whereLogic, List<ExpressionProxy> whereParams)
			throws IllegalArgumentException {

		//Query Builders
		StringBuffer pqlBuffer = null;
		try{pqlBuffer = new StringBuffer(create(tableName, projectionParams));}
		catch(IllegalArgumentException iex){throw iex;}

		if(whereParams != null 
				&& whereParams.size() > 0
				&& !isAllParamEmpty(whereParams.toArray())){

			if(pqlBuffer.length() > 0){
				pqlBuffer.append(" WHERE ");
				int count = 0;
				for(ExpressionProxy param : whereParams){
					if(param.getProperty().trim().equals("")){continue;}
					if(count++ != 0){pqlBuffer.append( " " + whereLogic.name() + " ");}
					pqlBuffer.append(param.getProperty() + " " + param.getType().toString() + " " + MARKER);
				}
			}
		}
		//
		return pqlBuffer.toString();
	}

    @Deprecated
    @SuppressWarnings("Duplicates")
	public static String create(String tableName, String[]projectionParams, Logic whereLogic, String[] whereParams)
			throws IllegalArgumentException {
		return SQLSelectQuery.create(tableName, projectionParams, whereLogic, ExpressionProxy.createListFrom(whereParams, Operator.EQUAL));
	}

    @Deprecated
    @SuppressWarnings("Duplicates")
	public static String create(String tableName, String...projectionParams)
			throws IllegalArgumentException {
		//Query Builders
		StringBuffer pqlBuffer = new StringBuffer("SELECT ");
		if(projectionParams != null && projectionParams.length > 0){
			int count = 0;
			for(String str : projectionParams){
				if(str.trim().equals("")){continue;}
				if(count++ != 0){pqlBuffer.append(", ");}
				pqlBuffer.append( QUANTIFIER + "." + str);
			}
			//If all passed parameter is empty
			if(count == 0){pqlBuffer.append(QUANTIFIER + "." + STARIC);}
		}else{
			pqlBuffer.append(QUANTIFIER + "." + STARIC);
		}
		pqlBuffer.append(" FROM "+ tableName + " " + QUANTIFIER);
		return pqlBuffer.toString();
	}

    @Override
    public String bindValueToString(JDBCDriverClass dialect) {
        StringBuffer buffer = new StringBuffer(toString(dialect));
        if(getWhereProperties() != null)
            buffer = bindValueToQueryBuffer(buffer, getWhereProperties());
        return buffer.toString();
    }

}