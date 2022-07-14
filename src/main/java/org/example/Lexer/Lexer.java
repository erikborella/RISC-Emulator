package org.example.Lexer;

import java.util.*;
import java.util.stream.Collectors;

public class Lexer {
    public static List<Token> Parse(String program) {
        Queue<Character> lexemes = stringToQueue(program);
        List<Token> tokens = new ArrayList<>();
        int lineCounter = 1;

        while (!lexemes.isEmpty()) {
            char lexeme = lexemes.remove();
            StringBuilder currentTokenValue = new StringBuilder();

            if (lexeme == '\n') {
                lineCounter++;
            }
            else if (lexeme == 'R' || lexeme == '#') {
                TokenType tokenType = (lexeme == 'R') ? TokenType.REGISTER : TokenType.LITERAL;

                while (!lexemes.isEmpty() && Character.isDigit(lexemes.peek())) {
                    currentTokenValue.append(lexemes.remove());
                }

                Token token = new Token(lineCounter, currentTokenValue.toString(), tokenType);
                tokens.add(token);
            }
            else if (Character.isAlphabetic(lexeme)) {
                currentTokenValue.append(lexeme);

                while (!lexemes.isEmpty() && Character.isLetterOrDigit(lexemes.peek())) {
                    currentTokenValue.append(lexemes.remove());
                }

                String value = currentTokenValue.toString();
                TokenType tokenType;

                if (!lexemes.isEmpty() && lexemes.peek() == ':') {
                    tokenType = TokenType.LABEL;
                    lexemes.remove();
                } else {
                    tokenType = getTokenType(value);
                }

                Token token = new Token(lineCounter, value, tokenType);
                tokens.add(token);
            }
        }

        return tokens;
    }

    public static List<String> getLinesSnapshot(String program) {
        List<String> linesSnapshot = new ArrayList<>(Arrays.asList(program.split("\n")));
        return linesSnapshot;
    }
    private static TokenType getTokenType(String value) {
        ArrayList<String> commands = new ArrayList<>(
                Arrays.asList(
                        "ADD",
                        "SUB",
                        "MUL",
                        "LDR",
                        "STR",
                        "MOV",
                        "CMP",
                        "B",
                        "BGT",
                        "BGE",
                        "BLT",
                        "BLE",
                        "BEQ",
                        "BNE"
                )
        );

        if (commands.contains(value))
            return TokenType.COMMAND;
        else
            return TokenType.VARIABLE;
    }

    private static Queue<Character> stringToQueue(String str) {
        Queue<Character> queue = new LinkedList<>(
                str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList())
        );

        return queue;
    }
}
