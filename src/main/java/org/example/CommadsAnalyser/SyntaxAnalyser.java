package org.example.CommadsAnalyser;

import org.example.Lexer.Token;
import org.example.Lexer.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class SyntaxAnalyser {
    public static void Analyse(List<Token> tokens, List<String> linesSnapshot) throws Exception {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == TokenType.COMMAND) {
                Command command = CommandsDefinition.commands.get(token.getValue());
                int parameterCount = command.getParameters().size();

                if (parameterCount > tokens.size() - (i+1)) {
                    throw new Exception(String.format("""
                            Erro na linha %d: \"%s\"
                            O Comando %s requer %d parametros
                            """,
                            token.getLine(), linesSnapshot.get(token.getLine()-1),
                            token.getValue(), parameterCount));
                }

                for (CommandParameter parameter : command.getParameters()) {
                    i++;
                    Token currentToken = tokens.get(i);

                    if (!parameter.getOptions().contains(currentToken.getType())) {
                        throw new Exception(String.format("""
                                Erro na linha %d: \"%s\"
                                O Comando \"%s\" requer um parametro \"%s\", mas recebeu um parametro \"%s\"
                                """,
                                currentToken.getLine(), linesSnapshot.get(token.getLine()-1),
                                token.getValue(),
                                parameter.getOptions().stream().map(Enum::toString).collect(Collectors.joining(", ")),
                                currentToken.getType().toString()));
                    }
                }
            }
        }
    }
}