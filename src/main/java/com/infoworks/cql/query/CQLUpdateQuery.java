package com.infoworks.cql.query;

import com.infoworks.sql.query.SQLUpdateQuery;
import com.infoworks.sql.query.models.ExpressionInterpreter;

public class CQLUpdateQuery extends SQLUpdateQuery {
    @Override
    protected void prepareWhereExpression(ExpressionInterpreter whereExpression) {
        //FIXME:
        String clause = whereExpression.interpret();
        clause = clause.replace("(", "");
        clause = clause.replace(")", "");
        whereBuffer.append(" WHERE " + clause);
    }
}
