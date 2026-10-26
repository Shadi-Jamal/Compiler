package de.uniwue.jpp.compiler.syntax;

public class Expression {
    ExpressionKind kind; int left; int right; int primaryToken;
    public Expression(ExpressionKind kind, int left, int right, int primaryToken) {
        this.kind=kind;
        this.left=left;
        this.right=right;
        this.primaryToken=primaryToken;
    }

    public ExpressionKind getKind() {
        return kind;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getValue() {
        if (kind == ExpressionKind.Identifier || kind == ExpressionKind.Number) {
            return left;
        }
        else return right;
    }

    public int getPrimaryToken() {
        return primaryToken;
    }

    public static Expression makeNumber(int value, int primaryToken) {
        return new Expression(ExpressionKind.Number,value,0,primaryToken);
    }

    public static Expression makeIdentifier(int index, int primaryToken) {
        return new Expression(ExpressionKind.Identifier,index,0,primaryToken);
    }

    public int getFirst(AST ast) {
        if (kind==ExpressionKind.Number|| kind==ExpressionKind.Identifier||kind==ExpressionKind.Let)
            return getPrimaryToken();
        //Add, Sub, Mul, Div, Mod
        else if (kind==ExpressionKind.Add||kind==ExpressionKind.Sub||kind==ExpressionKind.Mul||kind==ExpressionKind.Div||kind==ExpressionKind.Mod)
            return ast.getExpression(left).getFirst(ast);
        else return primaryToken;
    }

    public int getLast(AST ast) {
        if (kind==ExpressionKind.Number|| kind==ExpressionKind.Identifier)
            return getPrimaryToken();
        else if (kind==ExpressionKind.Let) return ast.getExpression(right+1).getLast(ast);
        else if (kind==ExpressionKind.Add||kind==ExpressionKind.Sub||kind==ExpressionKind.Mul||kind==ExpressionKind.Div||kind==ExpressionKind.Mod) {
            return ast.getExpression(right).getLast(ast);
        }else return primaryToken;
    }

    public TokenLocation getLocation(AST ast) {
        return new TokenLocation(getFirst(ast),getLast(ast));
    }
}
