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

public class VHDLGenerator {
    
    private static final String PROMPT = ">> ";
    private static int r;                       // radix
    private static int n;                       // precision
    private static DigitSet D;                  // digit set
    
    private static void interactiveMode() {
        printPrompt();
        System.out.println("Enter radix (r >= 4) :");
        r = getInteger();
        
        printPrompt();
        System.out.println("Enter precision (n >= 1) :");
        n = getInteger();
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

    public static void main(String[] args) {
        
        if (args.length == 0)
            interactiveMode();
        else if (args.length != 2) {
            System.out.println("Usage: VHDLGenerator <radix> <precision>\n");
            return;
        }
        else {
            try {
                r = Integer.parseInt(args[0]);
                n = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value for radix or precision\n");
                return;
            }
        }
        
        // Check correct values for r and n
        if (r < 4) {
            System.out.println("Radix has to be greater than or equal to 4\n");
            return;
        } else if (n < 1) {
            System.out.println("Precision has to be at least 1\n");
            return;
        }
        
        D = new DigitSet(r);
        printPrompt();
        System.out.printf("Your radix is %d, precision is %d and your digit set is %n%s%s%n",
                r, n, PROMPT, D);
        
        System.out.println();
    }
    
}
