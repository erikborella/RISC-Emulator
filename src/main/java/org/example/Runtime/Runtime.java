package org.example.Runtime;

import org.example.Lexer.Token;
import org.example.Lexer.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

public class Runtime {
    private final Map<String, Integer> memory;
    private final Map<String, Integer> labels;

    private final FlagsRegister flags;
    private final int[] registers;

    private final List<Token> tokens;
    private final List<String> linesSnapshot;
    private int instructionPointer;

    private final boolean strictMode;

    public Runtime(List<Token> tokens, List<String> linesSnapshot, Map<String, Integer> labels, int registersNumber) {
        this.labels = labels;
        this.memory = new HashMap<>();

        flags = new FlagsRegister();
        this.registers = new int[registersNumber];

        this.tokens = tokens;
        this.linesSnapshot = linesSnapshot;

        this.instructionPointer = 0;

        this.strictMode = false;
    }

    public Runtime(List<Token> tokens, List<String> linesSnapshot, Map<String, Integer> labels, int registersNumber,
                   boolean strictMode) {
        this.labels = labels;
        this.memory = new HashMap<>();

        flags = new FlagsRegister();
        this.registers = new int[registersNumber];

        this.tokens = tokens;
        this.linesSnapshot = linesSnapshot;

        this.instructionPointer = 0;

        this.strictMode = strictMode;
    }

    public void executeNext() throws Exception {
        Token currentToken = this.getCurrentToken();

        if (currentToken.getType() == TokenType.COMMAND) {
            this.executeCommand();
        } else {
            this.instructionPointer++;
        }
    }


    public boolean hasNext() {
        return instructionPointer != tokens.size();
    }

    public Map<String, Integer> getMemory() {
        return memory;
    }

    public FlagsRegister getFlags() {
        return flags;
    }

    public int[] getRegisters() {
        return registers;
    }
    
    public Token getCurrentToken() {
        return this.tokens.get(this.instructionPointer);
    }

    private Token getCurrentTokenAndAdvance() {
        Token currentToken = this.getCurrentToken();
        this.instructionPointer++;

        return currentToken;
    }

    public String getCurrentLineSnapshot() {
        Token current = this.getCurrentToken();
        return this.linesSnapshot.get(current.getLine()-1);
    }

    private void executeCommand() throws Exception {
        Token currentCommand = this.getCurrentTokenAndAdvance();

        switch (currentCommand.getValue()) {
            case "ADD" -> executeAdd();
            case "SUB" -> executeSub();
            case "MUL" -> executeMul();
            case "LDR" -> executeLdr();
            case "MOV" -> executeMov();
            case "STR" -> executeStr();
            case "CMP" -> executeCmp();
            case "B" -> executeB();
            case "BEQ" -> executeBeq();
            case "BNE" -> executeBne();
            case "BLT" -> executeBlt();
            case "BLE" -> executeBle();
            case "BGT" -> executeBgt();
            case "BGE" -> executeBge();
        }
    }

    private void verifyIfRegisterIsValid(int registerNumber, Token currentToken) throws Exception {
        if (registerNumber >= this.registers.length) {
            throw new Exception(String.format("""
                    Erro na linha %d: "%s"
                    Registrador "R%d" eh invalido. Registradores validos sao R0-R%d
                    """,
                    currentToken.getLine(), this.linesSnapshot.get(currentToken.getLine()-1),
                    registerNumber, this.registers.length-1));
        }
    }

    private int getRegisterAndAdvance() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();
        int register = Integer.parseInt(currentToken.getValue());

        this.verifyIfRegisterIsValid(register, currentToken);

        return register;
    }

    private int getRegisterValueAndAdvance() throws Exception {
        int register = this.getRegisterAndAdvance();

        return this.registers[register];
    }

    private int getRegisterValueOrLiteralAndAdvance() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();
        int value = Integer.parseInt(currentToken.getValue());

        if (currentToken.getType() == TokenType.REGISTER) {
            this.verifyIfRegisterIsValid(value, currentToken);
            return this.registers[value];
        }

        return value;
    }

    private int getVariableValueOrLiteralAndAdvance() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (currentToken.getType() == TokenType.VARIABLE) {
            if (this.strictMode && !this.memory.containsKey(currentToken.getValue())) {
                throw new Exception(String.format("""
                    Erro na linha %d "%s",
                    Variavel "%s" nao esta definda
                    Voce esta no modo estrito, defina as suas variaveis antes usando STR
                    """,
                        currentToken.getLine(), linesSnapshot.get(currentToken.getLine()-1),
                        currentToken.getValue()));
            }

            return this.memory.getOrDefault(currentToken.getValue(), 0);
        }

        return Integer.parseInt(currentToken.getValue());
    }

    private void setInstructionPointerToLabel(Token currentToken) throws Exception {
        String label = currentToken.getValue();

        if (!this.labels.containsKey(label)) {
            throw new Exception(String.format("""
                    Erro na linha %d: "%s"
                    Label "%s" nao foi encontrada
                    """,
                    currentToken.getLine(), this.linesSnapshot.get(currentToken.getLine()-1),
                    label));
        }

        int labelIndex = this.labels.get(label);
        this.instructionPointer = labelIndex + 1;
    }

    private void executeAdd() throws Exception {
        int resultRegister = this.getRegisterAndAdvance();

        int value1 = this.getRegisterValueAndAdvance();

        int value2 = this.getRegisterValueOrLiteralAndAdvance();

        int result = value1 + value2;
        this.registers[resultRegister] = result;
    }

    private void executeSub() throws Exception {
        int resultRegister = this.getRegisterAndAdvance();

        int value1 = this.getRegisterValueAndAdvance();

        int value2 = this.getRegisterValueOrLiteralAndAdvance();

        int result = value1 - value2;
        this.registers[resultRegister] = result;
    }

    private void executeMul() throws Exception {
        int resultRegister = this.getRegisterAndAdvance();

        int value1 = this.getRegisterValueAndAdvance();

        int value2 = this.getRegisterValueOrLiteralAndAdvance();

        int result = value1 * value2;
        this.registers[resultRegister] = result;
    }

    private void executeLdr() throws Exception {
        int resultRegister = this.getRegisterAndAdvance();
        int value = this.getVariableValueOrLiteralAndAdvance();

        this.registers[resultRegister] = value;
    }

    private void executeMov() throws Exception {
        int resultRegister = this.getRegisterAndAdvance();
        int value = this.getRegisterValueOrLiteralAndAdvance();

        this.registers[resultRegister] = value;
    }

    private void executeStr() throws Exception {
        int sourceRegisterValue = this.getRegisterValueAndAdvance();
        String variable = this.getCurrentTokenAndAdvance().getValue();

        this.memory.put(variable, sourceRegisterValue);
    }

    private void executeCmp() throws Exception {
        int value1 = this.getRegisterValueAndAdvance();
        int value2 = this.getRegisterValueAndAdvance();

        this.flags.setFlags(value1, value2);
    }

    private void executeB() throws Exception {
        Token currentToken = this.getCurrentToken();

        this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBeq() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (this.flags.isEquals())
            this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBne() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (!this.flags.isEquals())
            this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBlt() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (this.flags.isLess())
            this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBle() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (this.flags.isLess() || this.flags.isEquals())
            this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBgt() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (this.flags.isGreater())
            this.setInstructionPointerToLabel(currentToken);
    }

    private void executeBge() throws Exception {
        Token currentToken = this.getCurrentTokenAndAdvance();

        if (this.flags.isGreater() || this.flags.isEquals())
            this.setInstructionPointerToLabel(currentToken);
    }
}
