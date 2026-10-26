package de.uniwue.jpp.compiler.syntax;

import java.util.HashMap;
import java.util.Map;

public class ASTInterfaceImplementierung implements AST{

    Map<Integer,Expression> expressionMap;
    int index;
    public ASTInterfaceImplementierung() {
        this.expressionMap =new HashMap<>() ;
        this.index = 0;
    }
    @Override
    public int addExpression(Expression expression) {
        int currentindex=index;
        expressionMap.put(currentindex,expression);
        index++;
        return currentindex;
    }

    @Override
    public Expression getExpression(int expression) {
        return expressionMap.get(expression);
    }
    public static AST create() {
        return new ASTInterfaceImplementierung();
    }

}
