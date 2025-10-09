package com.infoworks.cql.query;

import com.infoworks.sql.query.QueryType;
import com.infoworks.sql.query.SQLQuery;
import com.infoworks.sql.query.builder.*;
import com.infoworks.sql.query.models.Expression;

public class CQLQuery extends SQLQuery {

    public static class Builder extends AbstractQueryBuilder {

        public Builder(QueryType type){
            factory(type);
        }

        @SuppressWarnings("unchecked")
        public <T extends SQLQuery> T build(){
            return (T) super.build();
        }

        protected SQLQuery factory(QueryType type){
            SQLQuery temp = null;
            switch (type) {
                case SELECT:
                    temp = new CQLSelectQuery();
                    break;
                case UPDATE:
                    temp = new CQLUpdateQuery();
                    break;
                case INSERT:
                    temp = new CQLInsertQuery();
                    break;
                case DELETE:
                    temp = new CQLDeleteQuery();
                    break;
                default:
                    temp = super.factory(type);
                    break;
            }
            setTempType(type);
            setTempQuery(temp);
            return temp;
        }

        @Override
        public LimitBuilder orderBy(String... columns) {
            return this;
        }

        @Override
        public QueryBuilder addLimit(Integer limit, Integer offset) {
            //Cassandra not support OFFSET:
            return super.addLimit(limit, 0);
        }

        @Override
        public HavingBuilder groupBy(String... columns) {
            return this;
        }

        @Override
        public OrderByBuilder having(Expression expression) {
            return this;
        }
    }

}
