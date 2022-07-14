package org.example;

import org.example.Lexer.Lexer;
import org.example.Runtime.PreRuntime;

public class Main {
    public static void main(String[] args) throws Exception {
        String program = """
                ADD R0, R0, R0
                ADD R0, R0, #10
                LDR R0, A
                LDR R0, R1
                MOV R0, R1
                MOV R0, #10
                STR R0, VAR
                CMP R0, R1
                B WHILE
                """;

        PreRuntime.PrepareRuntime(program, 100);
    }
}