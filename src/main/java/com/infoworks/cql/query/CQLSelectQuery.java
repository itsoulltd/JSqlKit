package com.infoworks.cql.query;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.sql.query.SQLSelectQuery;
import com.infoworks.sql.query.models.Expression;

public class CQLSelectQuery extends SQLSelectQuery {

    @Override
    protected void prepareWhereExpression(Expression whereExpression) {
        //FIXME:
        String clause = whereExpression.interpret();
        clause = clause.replace("(", "");
        clause = clause.replace(")", "");
        pqlBuffer.append(" WHERE " + clause);
    }

    @Override
    protected void appendLimit(StringBuffer pqlBuffer, JDBCDriverClass dialect) {
        //super.appendLimit(pqlBuffer, dialect);
        if (limit > 0) {
            if (pqlBuffer.toString().contains("LIMIT"))
                return;
            if (pqlBuffer.toString().endsWith("ALLOW FILTERING")) {
                int start = pqlBuffer.length() - "ALLOW FILTERING".length() - 1;
                int end = pqlBuffer.length();
                pqlBuffer.replace(start, end, " LIMIT " + limit + " ALLOW FILTERING");
                return;
            }
            if(!pqlBuffer.toString().endsWith("ALLOW FILTERING")) {
                pqlBuffer.append(" LIMIT " + limit + " ALLOW FILTERING");
            }
        }
    }
}
