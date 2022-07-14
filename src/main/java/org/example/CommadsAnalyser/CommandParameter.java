package org.example.CommadsAnalyser;

import org.example.Lexer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CommandParameter {
    private final List<TokenType> options;

    CommandParameter(TokenType... options) {
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    List<TokenType> getOptions() {
        return options;
    }
}
