/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

public enum Operation {

    ADD('+', "addition"),
    SUB('-', "subtraction"),
    MULT('*', "multiplication"),
    DIV('/', "division"),
    UNDEF(' ', "undefined");

    char operator;
    String name;

    Operation(char op, String name) {
        this.operator = op;
        this.name = name;
    }

    public static Operation set(char op) {
        switch (op) {
            case '+':
                return ADD;
            case '-':
                return SUB;
            case '*':
                return MULT;
            case '/':
                return DIV;
            default:
                return UNDEF;
        }
    }
}
