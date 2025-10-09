package com.infoworks.cql.query;

import com.infoworks.sql.query.SQLDeleteQuery;
import com.infoworks.sql.query.models.Expression;

public class CQLDeleteQuery extends SQLDeleteQuery {
    @Override
    protected void prepareWhereExpression(Expression whereExpression) {
        //FIXME:
        String clause = whereExpression.interpret();
        clause = clause.replace("(", "");
        clause = clause.replace(")", "");
        pqlBuffer.append(" WHERE " + clause);
    }
}
