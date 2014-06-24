/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * Online arithmetic generator for FPGAs
 * May 2014
 */

package vhdlgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class VHDLGenerator {
    
    private static final String PROMPT = ">> ";
    private static int r;                       // radix
    private static int n;                       // precision
    private static DigitSet D;                  // digit set
    private static Operation op;
    
    public static List<Character> operators = Arrays.asList('+', '-', '*', '/');
    
    private enum Operation {
        ADD   ('+', "addition"),
        SUB   ('-', "subtraction"),
        MULT  ('*', "multiplication"),
        DIV   ('/', "division"),
        UNDEF (' ', "undefined");
        
        char operator;
        String name;
        
        Operation (char op, String name) {
            this.operator = op;
            this.name = name;
        }
        
        public static Operation set (char op) {
            switch (op) {
                case '+': return ADD;
                case '-': return SUB;
                case '*': return MULT;
                case '/': return DIV;
                default : return UNDEF;
            }
        }
    }
    
    private static void interactiveMode() {
        printPrompt();
        System.out.println("Enter radix (r >= 4) :");
        r = getInteger();
        
        printPrompt();
        System.out.println("Enter precision (n >= 1) :");
        n = getInteger();
        
        printPrompt();
        System.out.println("Enter operation ( + | - | * | / ) :");
        op = Operation.set(getOperator());
    }
    
    private static void printPrompt() {
        System.out.print(PROMPT);
    }
    
    private static String getInput() {
        String input;
        
        try {
            InputStreamReader  consoleReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(consoleReader);
            
            input = reader.readLine();
            
            /* You don't want to close the standard input stream.... */
//            try (BufferedReader reader = new BufferedReader(consoleReader)) {
//                input = reader.readLine();
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return input;
    }
    
    private static Integer getInteger() {
        boolean obtained = false;
        Integer result = 0;
        
        while (!obtained) {
            try {
                result = Integer.parseInt(getInput());
                obtained = true;
            } catch (NumberFormatException e) {
                obtained = false;
                printPrompt();
                System.out.println("Try again:");
            }
        }
        
        return result;
    }
    
    private static Character getOperator() {
        boolean obtained = false;
        Character result = ' ';
        
        while (!obtained) {
            String input = getInput();
            if (input.length() == 1) {
                result = input.charAt(0);
            }
            
            obtained = operators.contains(result);
            
            if (!obtained) {
                printPrompt();
                System.out.println("Try again: ");
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        
        if (args.length == 0) {
            interactiveMode();
        } else if (args.length != 3) {
            System.out.println("Usage: VHDLGenerator <radix> <precision> <operation>\n");
            return;
        } else {
            try {
                r = Integer.parseInt(args[0]);
                n = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value for radix or precision\n");
                return;
            }
            
            // Check for correct operator
            if (args[2].length() != 1 || !operators.contains(args[2].charAt(0))) {
                printPrompt();
                System.out.println("Incorrect operator. Please choose from: +, -, *, / \n");
                System.exit(-1);
            } else {
                op = Operation.set(args[2].charAt(0));
            }
        }

        // Check correct values for r and n
        if (r < 4) {
            printPrompt();
            System.out.println("Radix has to be greater than or equal to 4\n");
            System.exit(-1);
        } else if (n < 1) {
            printPrompt();
            System.out.println("Precision has to be at least 1\n");
            System.exit(-1);
        }
        
        // Print parameters
        D = new DigitSet(r);
        printPrompt();
        System.out.printf("Your radix is %d, precision is %d and your digit set is %n%s%s%n",
                r, n, PROMPT, D);
        printPrompt();
        System.out.printf("The operation to be performed is %s.%n", op.name);
        
        System.out.println();
        
        
        // Generate an adder
        //DigitSet D = new DigitSet(r);
        Adder add = new Adder(r, D);
        
        System.out.println(System.getProperty("user.dir"));
        add.generate();
    }
    
}
