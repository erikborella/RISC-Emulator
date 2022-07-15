package org.example;

import org.example.Lexer.Token;
import org.example.Runtime.FlagsRegister;
import org.example.Runtime.PreRuntime;
import org.example.Runtime.Runtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: <code path> " +
                    "-r || --registers <number of registers> " +
                    "[-s || --strictMode]");
            System.exit(1);
        }

        if (!args[1].equals("-r") && args[1].equals("--registers")) {
            System.out.println("""
                    Please inform the number of registers with -r or --registers
                    Example: <path to code> -r 5
                    """);
            System.exit(1);
        }

        boolean strictMode = args.length >= 4 && (args[3].equals("-s") || args[3].equals("--strictMode"));

        int registersNumber = Integer.parseInt(args[2]);
        String program = readFile(args[0]);

        Runtime runtime = PreRuntime.PrepareRuntime(program, registersNumber, strictMode);
        run(runtime);
    }

    private static void run(Runtime runtime) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String lastInput = "";

        while (runtime.hasNext()) {
            while (true) {
                Token currentToken = runtime.getCurrentToken();
                System.out.printf("next: %d -> %s\n",
                        currentToken.getLine(), runtime.getCurrentLineSnapshot());

                System.out.print("> ");
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    input = lastInput;
                } else {
                    lastInput = input;
                }

                if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("next")) {
                    runtime.executeNext();
                    break;
                } else if (input.equalsIgnoreCase("r") || input.equalsIgnoreCase("registers")) {
                    showRegisters(runtime);
                } else if (input.equalsIgnoreCase("m") || input.equalsIgnoreCase("memory")) {
                    showMemory(runtime);
                } else if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("flags")) {
                    showFlags(runtime);
                }
            }
        }
    }

    private static String readFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readString(path);
    }

    private static void showRegisters(Runtime runtime) {
        int[] registers = runtime.getRegisters();

        System.out.println("\n\nREGISTERS:");

        for (int i = 0; i < registers.length; i++) {
            System.out.println("----------------------------");
            System.out.printf("R%d\t=\t%d\n", i, registers[i]);
        }

        System.out.println("----------------------------\n");
    }

    private static void showMemory(Runtime runtime) {
        Map<String, Integer> memory = runtime.getMemory();

        System.out.println("\n\nMEMORY:");

        for (var variable : memory.entrySet()) {
            System.out.println("----------------------------");
            System.out.printf("%s\t=\t%d\n", variable.getKey(), variable.getValue());
        }

        System.out.println("----------------------------\n");
    }

    private static void showFlags(Runtime runtime) {
        FlagsRegister flagsRegister = runtime.getFlags();

        System.out.println("\n\nFLAGS:");
        System.out.println("----------------------------");
        System.out.printf("==\t=\t%b\n", flagsRegister.isEquals());
        System.out.println("----------------------------");
        System.out.printf(">\t=\t%b\n", flagsRegister.isGreater());
        System.out.println("----------------------------");
        System.out.printf("<\t=\t%b\n", flagsRegister.isLess());
        System.out.println("----------------------------\n");
    }
}