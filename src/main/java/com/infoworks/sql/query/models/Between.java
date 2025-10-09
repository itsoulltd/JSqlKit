package com.infoworks.sql.query.models;

import com.infoworks.orm.Property;

public class Between extends ExpressionProxy {

    private Property secondValueProperty;

    public Property getSecondValueProperty() {
        return secondValueProperty;
    }

    public Between(Property first, Property second, Operator type) {
        super(first, type);
        this.secondValueProperty = second;
    }

    @Override
    public String interpret() {
        if (getValueProperty() != null && getSecondValueProperty() != null) {
            if (Character.isWhitespace(getQuantifier()) == false) {
                return String.format("%s.%s %s %s_left AND %s_right", getQuantifier(), getProperty(), getType().toString()
                        , getExpressMarker(), getExpressMarker());
            } else {
                return String.format("%s %s %s AND %s", getProperty(), getType().toString(), getMARKER(), getMARKER());
            }
        }
        return super.interpret();
    }

    @Override
    public ExpressionProxy[] resolve() {
        return new ExpressionProxy[] {this, new ExpressionProxy(getSecondValueProperty(), getType())};
    }

}
