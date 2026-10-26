package de.uniwue.jpp.compiler.syntax;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.jpp.compiler.util.NameCache;

public class Lexer {
    String text;
    NameCache names;
    int start;
    int position;
    public Lexer(String text, NameCache names) {
        this.text=text;
        this.names=names;
    }

    public Lexer(String text, NameCache names, int start) {
        this.text=text;
        this.names=names;
        this.position=start;
    }

    public static List<Token> lex(String text, NameCache names) {
        List<Token> tokenList=new ArrayList<>();
        Lexer lexer=new Lexer(text,names);
        for (Token token = lexer.lexToken(); token.kind != TokenKind.EOF; token = lexer.lexToken()) {
            tokenList.add(token);
        }
        tokenList.add(new Token(TokenKind.EOF,lexer.start));
        return tokenList;
    }

    public int getCurrent() {
       return this.position;
    }

    public void skipWhitespace() {
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }

    }

    //Lext einen Namen und erstellt ein dazu passendes Token.
    public Token lexName() {
        int startPosition = position;
        StringBuilder name = new StringBuilder();
        if (position<text.length()&& Character.isLetter(text.charAt(position))){
            name.append(text.charAt(position));
            position++;
        }
        while (position < text.length() && (Character.isLetterOrDigit(text.charAt(position)) || text.charAt(position) == '_')) {
            name.append(text.charAt(position));
            position++;
        }

        if (name.toString().equals("in")) {
            return new Token(TokenKind.InKeyword,startPosition);
        } else if (name.toString().equals("let")) return new Token(TokenKind.LetKeyword,startPosition);
        else return new Token(TokenKind.Identifier,startPosition,names.cacheName(name.toString()));
    }

    public Token lexNumber() {
        StringBuilder number = new StringBuilder();
        int startPosition = position;
        if (position < text.length() ) {
            if (text.charAt(position) >= '0' && text.charAt(position) <= '9'){
                number.append(text.charAt(position));
                position++;
            }

            while (position < text.length() && ((text.charAt(position) >= '0' && text.charAt(position) <= '9') || text.charAt(position) == '_')) {
                if (text.charAt(position) != '_') {
                    number.append(text.charAt(position));
                }
                position++;
            }
        }  return new Token(TokenKind.Number, startPosition,Integer.parseInt(number.toString()));

    }

    public Token lexToken() {
        skipWhitespace();
        start = position;
        if (isAtEnd()) {
            return new Token(TokenKind.EOF, position);
        } else if (position < text.length() && Character.isLetter(text.charAt(position))) {
            return lexName();
        } else if (position < text.length() && Character.isDigit(text.charAt(position))) {
            return lexNumber();
        } else if (position < text.length()) {
            char c = text.charAt(position);
            position++;
            if (c == '+') {return new Token(TokenKind.Plus, start);}
            else if (c == '-') return new Token(TokenKind.Minus, start);
            else if (c == '*') return new Token(TokenKind.Times, start);
            else if (c == '/') return new Token(TokenKind.Divide, start);
            else if (c == '%') return new Token(TokenKind.Modulo, start);
            else if (c == '=') return new Token(TokenKind.Equals, start);
            else if (c == '(') return new Token(TokenKind.OpenParen, start);
            else if (c == ')') return new Token(TokenKind.CloseParen, start);
            else {return new Token(TokenKind.Unknown, start);}
        }
        return new Token(TokenKind.Unknown, position);
    }

        public List<Token> lexAll () {
            List<Token> tokenList = new ArrayList<>();
            while (position < text.length()) {
                skipWhitespace();
                if (position >= text.length()) {
                    break;
                }
                Token token = lexToken();
                tokenList.add(token);
            }
            tokenList.add(new Token(TokenKind.EOF, position));
            return tokenList;
        }
        public boolean isAtEnd () {
           return position >= text.length() || text.charAt(position) == '\0';
        }

}



