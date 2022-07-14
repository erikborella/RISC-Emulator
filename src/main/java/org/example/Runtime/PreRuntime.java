package org.example.Runtime;

import org.example.CommadsAnalyser.SyntaxAnalyser;
import org.example.Lexer.Lexer;
import org.example.Lexer.Token;
import org.example.Lexer.TokenType;

import java.util.*;

public class PreRuntime {

    public static Runtime PrepareRuntime(String program, int registersNumber) throws Exception {
        List<Token> tokens = Lexer.Parse(program);
        List<String> linesSnapshot = Lexer.getLinesSnapshot(program);

        Map<String, Integer> labels = indexLabels(tokens, linesSnapshot);

        SyntaxAnalyser.Analyse(tokens, linesSnapshot);

        return null;
    }

    private static Map<String, Integer> indexLabels(List<Token> tokens, List<String> linesSnapshot) throws Exception {
        Map<String, Integer> labels = new HashMap<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == TokenType.LABEL) {
                String tokenValue = token.getValue();

                if (labels.containsKey(tokenValue)) {
                    int alreadyDefinedLabelPosition = labels.get(tokenValue);
                    Token alreadyDefinedLabel = tokens.get(alreadyDefinedLabelPosition);

                    throw new Exception(String.format("""
                                    Erro na linha %d: %s
                                    -> Label "%s" ja foi definida antes em:
                                    -> Linha %d: "%s\"""",
                            token.getLine(), linesSnapshot.get(token.getLine()-1), tokenValue,
                            alreadyDefinedLabel.getLine(), linesSnapshot.get(alreadyDefinedLabel.getLine()-1)));
                }
                else {
                    labels.put(tokenValue, i);
                }
            }

        }

        return labels;
    }
}
