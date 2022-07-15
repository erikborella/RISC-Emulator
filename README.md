# RISC-Emulator

## Usage:
`java -jar RISCEmulator.jar <code path> (-r || --registers) <numbers of registers> [-s || --strictMode]` \

Example:
`java -jar RISCEmulator.jar code.asm -r 5 -s` \

> The Strict Mode doesn't allow you to read from undefined variables. They need to be defined using `STR` command

## Instructions Set:
| Command | Parameters               | Description                                                              | Example         |
|---------|--------------------------|--------------------------------------------------------------------------|-----------------|
| ADD     | REG, REG, REG \| LITERAL | Sum the second and third parameter and store in the first parameter      | ADD R0, R1, #10 |
| SUB     | REG, REG, REG \| LITERAL | Subtract the second and third parameter and store in the first parameter | SUB R1, R3, R0  |
| MUL     | REG, REG, REG \| LITERAL | Multiply the second and third parameter and store in the first parameter | MUL R1, R1, #3  |
| LDR     | REG, VARIABLE \| LITERAL | Load the value of a variable or literal to the first parameter           | LDR R0, sum     |
| MOV     | REG, REG, LITERAL        | Copy the value of a register or literal to the first parameter           | MOV R0, R1      |
| STR     | REG, VARIABLE            | Store the value of the first parameter in the variable                   | STR R0, n1      |
| CMP     | REG, REG                 | Compare the value of the two registers and set the flags                 | CMP R0, R1      |
| B       | LABEL                    | Jump to the label                                                        | B while         |
| BEQ     | LABEL                    | Jump to the label if the Equals flag is setted                           | BEQ if          |
| BNQ     | LABEL                    | Jump to the label if the Equals flag is not setted                       | BNQ label1      |
| BLT     | LABEL                    | Jump to the label if the Less flag is setted                             | BLT endif       |
| BLE     | LABEL                    | Jump to the label if the Less or Equals flag is setted                   | BLE iter        |
| BGT     | LABEL                    | Jump to the label if the Greater flag is setted                          | BGT endwhile    |
| BGE     | LABEL                    | Jump to the label if the Greater or Equals flag is setted                | BGE label2      |

## Running
When running the code it will be show to you an indication of what is the next line to be executed: \
![image](https://user-images.githubusercontent.com/27148919/179146207-a8d72555-2849-43ec-b50f-0c50edd8ff2b.png)

And now you can execute some commands:
1. `n` or `next`: Execute the next line: \
![image](https://user-images.githubusercontent.com/27148919/179146389-2d3da6b6-3700-4b47-9535-23d30b21c554.png)
2. `r` or `registers`: Show the value of all registers: \
![image](https://user-images.githubusercontent.com/27148919/179146751-96286792-a468-4cb0-849f-a1e86d551b14.png)
3. `m` or `memory`: Show the value of all variables: \
![image](https://user-images.githubusercontent.com/27148919/179146719-0aaa4085-7a94-4ec2-b08f-3e4b76e3d7a6.png)
4. `f` or `flags`: Show what flags are setted: \
![image](https://user-images.githubusercontent.com/27148919/179146891-79fc495c-ec6e-4284-98ed-2fb81ed197d2.png)


