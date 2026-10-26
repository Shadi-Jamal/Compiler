package de.uniwue.jpp.compiler.syntax;

public class TokenLocation {
    int first;
    int last;
    public TokenLocation(int first, int last) {
    this.first=first;
    this.last=last;
    }

    public int getFirst() {
        return this.first;
    }

    public int getLast() {
        return this.last;
    }

    public TokenLocation join(TokenLocation last) {
        return new TokenLocation(getFirst(),last.last);
    }
}
