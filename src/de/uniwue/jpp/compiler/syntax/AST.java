package de.uniwue.jpp.compiler.syntax;

public interface AST {
    public static AST create() {
        return new ASTInterfaceImplementierung();
    }

    public int addExpression(Expression expression);


    public Expression getExpression(int expression);



}
