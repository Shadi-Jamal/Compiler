package de.uniwue.jpp.compiler.syntax;

import de.uniwue.jpp.compiler.util.NameCache;
import jdk.jshell.Snippet;

public class Token {
    TokenKind kind;
    int position;
    int data;
    public Token(TokenKind kind, int position, int data) {
        this.kind=kind;
        this.position=position;
        this.data=data;
    }

    public Token(TokenKind kind, int position) {
        this.kind=kind;
        this.position=position;
        this.data=-1;
    }

    public TokenKind getKind() {
        return kind;
    }

    public int getPosition() {
        return position;
    }

    public int getData() {
        return data;
    }

    public int getLength(NameCache names, String source) {
        int startPos = position;
        int currentPos = startPos;
        if (kind==TokenKind.Number){
            while (currentPos < source.length() && Character.isDigit(source.charAt(currentPos))) {
                currentPos++;
            }
            return currentPos-startPos;
        }
        else if (kind==TokenKind.Identifier) {
           return names.getName(data).length();
        } else if (kind==TokenKind.LetKeyword)
            return 3;
        else if (kind==TokenKind.InKeyword) return 2;
        else if (kind==TokenKind.Unknown){
             return 1;
        } else if (kind==TokenKind.EOF) {
            return 0;
        }
        return 1;
    }
}
