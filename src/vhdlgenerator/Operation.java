/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

public enum Operation {

    ADD('+', "addition", "add"),
    SUB('-', "subtraction", "sub"),
    MULT('*', "multiplication", "mux"),
    DIV('/', "division", "div"),
    UNDEF(' ', "undefined", "udef");

    char operator;
    String name;
    String shortName;

    Operation(char op, String name, String shortName) {
        this.operator = op;
        this.name = name;
        this.shortName = shortName;
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
