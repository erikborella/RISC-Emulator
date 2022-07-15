package org.example.CommadsAnalyser;

import org.example.Lexer.TokenType;

import java.util.HashMap;

    class CommandsDefinition {
    static final HashMap<String, Command> commands = new HashMap<>();

    static {
        Command arithmetic = createArithmeticCommand();
        Command ldr = createLdr();
        Command mov = createMov();
        Command str = createStr();
        Command cmp = createCmp();
        Command branch = createBranchCommand();

        commands.put("ADD", arithmetic);
        commands.put("SUB", arithmetic);
        commands.put("MUL", arithmetic);
        commands.put("LDR", ldr);
        commands.put("MOV", mov);
        commands.put("STR", str);
        commands.put("CMP", cmp);
        commands.put("B", branch);
        commands.put("BEQ", branch);
        commands.put("BNE", branch);
        commands.put("BLT", branch);
        commands.put("BLE", branch);
        commands.put("BGT", branch);
        commands.put("BGE", branch);
    }

    private static Command createArithmeticCommand() {
        return new Command(
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.REGISTER, TokenType.LITERAL));
    }

    private static Command createLdr() {
        return new Command(
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.VARIABLE, TokenType.LITERAL));
    }

    private static Command createMov() {
        return new Command(
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.REGISTER, TokenType.LITERAL));
    }

    private static Command createStr() {
        return new Command(
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.VARIABLE));
    }

    private static Command createCmp() {
        return new Command(
                new CommandParameter(TokenType.REGISTER),
                new CommandParameter(TokenType.REGISTER));
    }

    private static Command createBranchCommand() {
        return new Command(
                new CommandParameter(TokenType.VARIABLE));
    }
}
