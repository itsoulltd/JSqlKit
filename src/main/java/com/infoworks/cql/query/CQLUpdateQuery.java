package com.infoworks.cql.query;

import com.infoworks.sql.query.SQLUpdateQuery;
import com.infoworks.sql.query.models.Expression;

public class CQLUpdateQuery extends SQLUpdateQuery {
    @Override
    protected void prepareWhereExpression(Expression whereExpression) {
        //FIXME:
        String clause = whereExpression.interpret();
        clause = clause.replace("(", "");
        clause = clause.replace(")", "");
        whereBuffer.append(" WHERE " + clause);
    }
}
