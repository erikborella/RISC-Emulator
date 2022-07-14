package org.example.Lexer;

public class Token {
    private int line;
    private String value;
    private TokenType type;

    public Token(int line, String value, TokenType type) {
        this.line = line;
        this.value = value;
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
