/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester implements Module {
    
    private final int r;                      // radix
    private final int n;                      // precision (no. of digits)
    private final int c;                      // bits per digit
    private final int a;                      // D = {-a, ..., a}
    private final int numTests;               // number of tests
    private final Operation op;
    public final String name;
    private final String filename;
    
    // Values of different variables for the generator
    private final Map<String, String> fields = new HashMap<>();
    
    // Arithmetic module
    AddSubModule addSub;
    
    public Tester(int r, int n, DigitSet D, int numTests, Operation op) {
        this.r = r;
        this.n = n;
        //this.D = D;
        this.c = D.c;
        this.a = D.a;
        this.op = op;
        this.numTests = numTests;
        
        addSub = new AddSubModule(r, n, a, op);
        
        name = op.shortName + "_tester_r" + r + "_n" + n;
        filename = name + ".v";
        
        fields.put("r", Integer.toString(r));
        fields.put("n", Integer.toString(n));
        fields.put("c", Integer.toString(c));
        fields.put("name", name);
    }
    
    @Override
    public void generate() {
        generateModuleDeclaration();
        generateCircuit();
        generateEndmodule();
    }
    
    @Override
    public String initialise() {
        String init;
        
        init = ""
                + "$testerName tester(\n"
                + "	.testSelect($testNo),\n"
                + "	.x($x),\n"
                + "	.y($y),\n"
                + "	.z($expected)\n"
                + ");\n"
                + "";
        
        return init;
    }
    
    private void generateModuleDeclaration() {
        List<String> schema = new ArrayList<>();
        
        fields.put("selSize", Integer.toString((int) Math.ceil(Math.log(numTests)/Math.log(2))));
        fields.put("selU", Integer.toString(Integer.parseInt(fields.get("selSize")) - 1));
        fields.put("inU", Integer.toString(n*c-1));
        fields.put("inL", "0");
        fields.put("outU", Integer.toString((n+1)*c-1));
        fields.put("outL", "0");
        
        schema.add( "module $name (" );
        
        schema.add( "   input       [$selU:0] testSelect," );
        
        schema.add( "   output reg\t[$inU:$inL] x," );
        schema.add( "\t\t\t\t// First number to add" );
        
        schema.add( "   output reg\t[$inU:$inL] y," );
        schema.add( "\t\t\t\t// Second number to add" );
        
        schema.add( "   output reg\t[$outU:$outL] z" );
        schema.add( "\t\t\t\t// Correct result" );
        
        schema.add( ");" );
        schema.add( "" );
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateCircuit() {
        List<String> schema = new ArrayList<>();
        
        String[] xyz;
        
        schema.add("");
        
        schema.add("always @(testSelect)");
        schema.add("    case (testSelect)");
        
        // Generate random tests
        for (int i = 0; i < numTests; i++) {
            if (i == 0)
                xyz = generateTest(0, 0);               // All zeros
            else if (i == 1)        
                xyz = generateTest(-a, -a);             // All min values
            else if (i == 2)
                xyz = generateTest(a, a);               // All max values
            else
                xyz = generateTest(-a, a);              // Any value in D
            
            schema.add("        $selSize'd" + i + ":");
            schema.add("            begin");
            schema.add("                x =       " + xyz[0] + ";");
            schema.add("                y =       " + xyz[1] + ";");
            schema.add("                z = " + xyz[2] + ";");
            schema.add("            end");
            schema.add("");
        }
        
        // Print the default case
        schema.add("        default:");
        schema.add("            begin");
        schema.add("                x = 0;");
        schema.add("                y = 0;");
        schema.add("                z = 0;");
        schema.add("            end");
        
        schema.add("    endcase");
        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private String[] generateTest(int min, int max) {
        String[] xyz = new String[3];
        
        int[] x = randomNumber(min, max);
        int[] y = randomNumber(min, max);
        int[] z = addSub.result(x, y);
        
        // Generate x
        xyz[0] = generateNumberString(x);
        
        // Generate y
        xyz[1] = generateNumberString(y);
        
        // Generate z
        xyz[2] = generateNumberString(z);
        
        return xyz;
    }
    
    private int[] randomNumber(int min, int max) {
        int[] num = new int[n];
        
        for (int i = 0; i < num.length; i++) {
            // Generate a random digit in the range {min, ..., max}
            int digit = min + new Double(Math.random()*(max-min)).intValue();
            num[i] = digit;
        }
        
        return num;
    }
    
    private String generateNumberString(int[] num) {
        String nums = "{";
        
        for (int i = 0; i < num.length; i++) {
            String digit = "";
            
            if (num[i] >= 0)
                digit = String.format("%s'd%d", fields.get("c"), num[i]);
            else
                digit = String.format("-%s'd%d", fields.get("c"), -num[i]);
            
            if (i < num.length - 1)
                digit += ", ";
            
            nums += digit;
        }
        
        nums += "}";
        
        return nums;
    }

    private void generateEndmodule() {
        List<String> schema = new ArrayList<>();

        schema.add("");
        schema.add("endmodule");
        schema.add("");

        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
}
