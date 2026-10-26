package de.uniwue.jpp.compiler.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.uniwue.jpp.compiler.error.Error;
import de.uniwue.jpp.compiler.error.ExpectedPrimaryExpressionError;
import de.uniwue.jpp.compiler.error.ExpectedTokenError;

// parser takes the stream of tokens from the lexer and turns it into an abstract syntax tree representing the (usually) program represented by the original text.
public class Parser {
    List<Token> tokens=new ArrayList<>();
    int position;
    List<Error> errorList=new ArrayList<>();
    AST ast = ASTInterfaceImplementierung.create();

    public Parser(List<Token> tokens) {
        this.tokens=tokens;
        this.position=0;
    }

    public Parser(List<Token> tokens, int position) {
        this.tokens=tokens;
        this.position=position;
    }

    public int getCurrentIndex() {
        return this.position; //Gibt den Tokenindex des derzeitigen Tokens zurück
    }

    public AST getAST() {
        return ast;
    }

    public List<Error> getErrors() {
       return errorList;
    }

    public Token currentToken() {
        if (tokens.isEmpty() || getCurrentIndex() >= tokens.size()) {
            return new Token(TokenKind.EOF, 0);
        }
        else return tokens.get(position);
    }

    public Token consumeToken() {
        if (tokens.isEmpty()) return new Token(TokenKind.EOF, 0);
        if (position >= tokens.size()) return new Token(TokenKind.EOF, 0);

        Token current = tokens.get(position);
        if (position<tokens.size()-1)
           position++;
        return current;
    }

    public Token expectToken(TokenKind kind) {
        if (tokens.isEmpty()) return new Token(TokenKind.EOF, 0);
        if (position >= tokens.size()) return new Token(TokenKind.EOF, 0);
        Token current = tokens.get(position);
        if (current.getKind() == kind) {
            return consumeToken();
        }
        else {
            TokenLocation tokenLocation=new TokenLocation(getCurrentIndex(),getCurrentIndex());
            ExpectedTokenError error=new ExpectedTokenError(kind,current.kind,tokenLocation);
            errorList.add(error);
            return new Token(kind,current.position);
        }
    }

    public Optional<Expression> parsePrimaryExpression() {
        Token currentedToken = currentToken();
        TokenKind tokenKind = currentedToken.getKind();
        if (currentedToken.kind==TokenKind.Number){
            return Optional.of(Expression.makeNumber(consumeToken().getData(),getCurrentIndex() - 1));
        }
        if (currentedToken.kind==TokenKind.Identifier){
            return Optional.of(Expression.makeIdentifier(consumeToken().getData(),getCurrentIndex() - 1));
        }
        if (currentedToken.kind==TokenKind.OpenParen){
            consumeToken();
            Optional<Expression> innerExpr = parseExpression();
            expectToken(TokenKind.CloseParen);
            return innerExpr;

        }
        else {
            TokenLocation tokenLocation = new TokenLocation(getCurrentIndex(), getCurrentIndex());
            errorList.add(new ExpectedPrimaryExpressionError(currentedToken.getKind(), tokenLocation));
            return Optional.empty();
        }
    }

    public Optional<Expression> parseBinaryMultiplicative() {
        Optional<Expression> optionalExpression = parsePrimaryExpression();
        if (optionalExpression.isEmpty()) return Optional.empty();
        Expression left = optionalExpression.get();
        while (true) {
            if (currentToken().getKind() == TokenKind.Times || currentToken().getKind() == TokenKind.Divide || currentToken().getKind() == TokenKind.Modulo) {
                Token operator = consumeToken();
                int currentIndexOperator = getCurrentIndex() - 1;
                Optional<Expression> right = parsePrimaryExpression();
                if (right.isEmpty()) return Optional.of(left);
                ExpressionKind kind;
                if (operator.getKind() == TokenKind.Times) {
                    kind = ExpressionKind.Mul;
                } else if (operator.getKind() == TokenKind.Divide) {
                    kind = ExpressionKind.Div;
                } else {
                    kind = ExpressionKind.Mod;
                }
                int leftIndex = getAST().addExpression(left);
                int rightIndex = getAST().addExpression(right.get());
                left=(new Expression(kind, leftIndex, rightIndex, currentIndexOperator));
            } else break;

        }return Optional.of(left);
    }

    public Optional<Expression> parseBinaryAdditive() {
        Optional<Expression> optionalExpression = parseBinaryMultiplicative();
        if (optionalExpression.isEmpty()) return Optional.empty();
        Expression left = optionalExpression.get();
        while (true) {
            if (currentToken().getKind() == TokenKind.Plus || currentToken().getKind() == TokenKind.Minus) {
                Token operator = consumeToken();
                int currentIndexOperator = getCurrentIndex() - 1;
                Optional<Expression> right = parseBinaryMultiplicative();
                if (right.isEmpty()) return Optional.of(left);
                ExpressionKind kind;
                if (operator.getKind() == TokenKind.Plus) kind = ExpressionKind.Add;
                else kind = ExpressionKind.Sub;
                int leftIndex = getAST().addExpression(left);
                int rightIndex = getAST().addExpression(right.get());
                left=(new Expression(kind, leftIndex, rightIndex, currentIndexOperator));
            } else break;

        }return Optional.of(left);
    }

    public Optional<Expression> parseLetExpression() {
        int letTokenshelIndex = getCurrentIndex();
        Token letTokenConsume = consumeToken();
        Token curent =currentToken();

        Token identifierToken = expectToken(TokenKind.Identifier);
        int nameshelIndex = identifierToken.getData();

        expectToken(TokenKind.Equals);
        Optional<Expression> assignedshelExpr = parseExpression();
        expectToken(TokenKind.InKeyword);
        Optional<Expression> innersherlExpression = parseExpression();
        if (innersherlExpression.isEmpty()||assignedshelExpr.isEmpty() ) {
            return Optional.empty();
        }
        AST ast = getAST();
        int i = ast.addExpression(assignedshelExpr.get());
        int i1 = ast.addExpression(innersherlExpression.get());
        return Optional.of(new Expression(ExpressionKind.Let, nameshelIndex,i, letTokenshelIndex));
    }

    public Optional<Expression> parseExpression() {
        Token current = currentToken();
        if (current.getKind() == TokenKind.LetKeyword) return parseLetExpression();
        else return parseBinaryAdditive();
    }

    public Optional<Integer> parseAll() {
        Optional<Expression> expression = parseExpression();
        if (expression.isEmpty()) return Optional.empty();
        expectToken(TokenKind.EOF);
        return Optional.of(getAST().addExpression(expression.get()));

    }

}
