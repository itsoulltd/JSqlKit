package com.infoworks.sql.query;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.orm.DataType;
import com.infoworks.orm.Property;
import com.infoworks.sql.query.models.*;

import java.util.List;

public class SQLScalarQuery extends SQLSelectQuery{
	
	public SQLScalarQuery() {
		this(ScalarType.COUNT);
	}

	public SQLScalarQuery(ScalarType type) {
		this.pqlBuffer = new StringBuffer("SELECT ");
		this.type = type;
	}
	
	private ScalarType type = ScalarType.COUNT;

	public ScalarType getType() {
		return type;
	}

	@Override
	protected String queryString(JDBCDriverClass dialect) throws IllegalArgumentException {
		if(getTableName() == null || getTableName().trim().equals("")){
			throw new IllegalArgumentException("Parameter Table must not be Null OR Empty.");
		}
		if(isAllParamEmpty(getColumns())){
			throw new IllegalArgumentException("All Empty Parameters!!! You nuts (:D");
		}
		return pqlBuffer.toString();
	}
	
	protected void prepareColumns(String[] columns){
		if(columns != null && columns.length > 0){
			String firstParam = columns[0];
			pqlBuffer.append(type.toString()+"(" + firstParam + ")");
		}else{
			pqlBuffer.append(type.toString()+"(" + "*" + ")");
		}
	}
	
	@Override
	protected void prepareTableName(String name) {
		pqlBuffer.append(" FROM " + name + " ");
	}
	
	public void setScalerClouse(Property prop, ExpressionProxy comps){
		if(prop != null){
			pqlBuffer.append("WHERE " + prop.getKey() +" "+ comps.getType().toString() +" ");
			if(prop.getType() == DataType.BOOL
					|| prop.getType() == DataType.INT
					|| prop.getType() == DataType.DOUBLE
					|| prop.getType() == DataType.FLOAT) {
				pqlBuffer.append(prop.getValue());
			}else{
				pqlBuffer.append("'" + prop.getValue() + "'");
			}
		}
	}
	
	@Override
	protected void prepareWhereParams(List<ExpressionProxy> whereParams) {
		if(whereParams != null && whereParams.size() > 0){
			pqlBuffer.append("WHERE " );
			int count = 0;
			for (ExpressionProxy ent : whereParams) {
				if(count++ != 0)
					pqlBuffer.append(" "+ getLogic().name() +" ");
				pqlBuffer.append(ent.getProperty()+ " " 
						+ ent.getType().toString() + " ?");
			}    			
		}
	}

	@Deprecated
    @SuppressWarnings("Duplicates")
	public static String createCountFunctionQuery(String tableName, String param, Logic logic, List<ExpressionProxy> whereParams){

		param = (param != null && param.length()>=1) ? param : "*";
		
		StringBuilder builder = new StringBuilder("SELECT ");
		builder.append(ScalarType.COUNT.toString()+"(" + param + ")");
		builder.append(" From " + tableName + " ");

		if(whereParams != null && whereParams.size() > 0){
			builder.append("Where " );
			int count = 0;
			for (ExpressionProxy ent : whereParams) {
				if(count++ != 0)
					builder.append(" "+ logic.name() +" ");
				builder.append(ent.getProperty() + " " 
						+ ent.getType().toString() + " ?");
			}    			
		}
		return builder.toString();
	}

	@Deprecated
    @SuppressWarnings("Duplicates")
	public static String createCountFunctionQuery(String tableName, String param, String whereParam, Operator type, Property paramValue){

		param = (param != null && param.length()>=1) ? param : "*";

		StringBuilder builder = new StringBuilder("SELECT ");
		builder.append(ScalarType.COUNT.toString()+"(" + param + ")");
		builder.append(" From " + tableName + " ");

		if(whereParam != null && paramValue != null){
			builder.append("Where " + whereParam +" "+ type.toString() +" ");
			if(paramValue.getType() == DataType.BOOL 
					|| paramValue.getType() == DataType.INT
					|| paramValue.getType() == DataType.DOUBLE
					|| paramValue.getType() == DataType.FLOAT) {
				builder.append(paramValue.getValue());
			}else{
				builder.append("'"+paramValue.getValue()+"'");
			}
		}

		return builder.toString();
	}

}