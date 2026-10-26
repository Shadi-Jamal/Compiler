package de.uniwue.jpp.compiler.syntax;

public enum TokenKind {
    EOF,
    Unknown,

    Number,
    Identifier,

    LetKeyword,
    InKeyword,

    Plus,
    Minus,
    Times,
    Divide,
    Modulo,

    Equals,

    OpenParen,
    CloseParen,
}
