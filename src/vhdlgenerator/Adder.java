/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Adder implements Module {
    
    private final int r;                        // radix
    private final int a;                        // D = {-a, ..., a}
    private final int c;                        // bits per digit
    private final DigitSet D; 
    private final String name;  
    private final String filename;              // Verilog file to be written to
    
    // Values of different variables for the generator
    private final Map<String, String> fields = new HashMap<>();
    
    public Adder(int r, DigitSet D) {
        this.r = r;
        this.D = D;
        this.a = D.a;                           // extracted for convenience
        c = (int) Math.ceil(Math.log(D.size)/Math.log(2));
        this.name = "online_adder_r" + Integer.toString(r);
        filename = name + ".v";
        
        fields.put("r", Integer.toString(r));
        fields.put("a", Integer.toString(a));
        fields.put("c", Integer.toString(c));
        fields.put("name", name);
    }
    
    public void generate() {
        generateModuleDeclaration();
        generateRegsParams();
        generateCircuit();
        generateTasks();
        generateEndmodule();
    }
    
    private void generateModuleDeclaration() {
        List<String> schema = new ArrayList<>();
        
        fields.put("u", Integer.toString(c-1));
        fields.put("l", Integer.toString(0));
        
        schema.add( "module $name (" );
        
        schema.add( "   input clk, reset, en," ); 
        schema.add("\t\t\t\t// asynchronous reset to clear all latches; "
                + "control signals: enable and clock" );
        
        schema.add( "   input       signed [$u:$l] xi," );
        schema.add( "\t\t// (i+2)th digit of the x input" );
        
        schema.add( "   input       signed [$u:$l] yi," );
        schema.add( "\t\t// (i+2)th digit of the y input" );
        
        schema.add( "   output reg  signed [$u:$l] zi" );
        schema.add( "\t\t// ith digit of the result" );
        
        schema.add( ");" );
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateRegsParams() {
        List<String> schema = new ArrayList<>();
        
        schema.add("");
        
        schema.add("parameter r = $r;");
        schema.add("parameter a = $a;");
        
        schema.add("");
        
        schema.add("reg signed [$u:$l] t;");
        schema.add("\t\t\t\t\t\t\t// transfer digit");
        
        schema.add("reg signed [$u:$l] w;");
        schema.add("\t\t\t\t\t\t\t// intermediate sum");
        
        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateCircuit() {
        List<String> schema = new ArrayList<>();
        
        schema.add("");

        schema.add(""
                + "always @(posedge clk, posedge reset)\n"
                + "	begin\n"
                + "		if (reset)\n"
                + "			begin\n"
                + "				// Clear all latches\n"
                + "				t = 0;\n"
                + "				w = 0;\n"
                + "				zi = 0;\n"
                + "			end\n"
                + "		else if (en)\n"
                + "			begin\n"
                + "				TW(xi, yi, t, w);\n"
                + "				SUM(t, w, zi);\n"
                + "			end\n"
                + "	end"
                + "");

        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateTasks() {
        List<String> schema = new ArrayList<>();
        
        fields.put("tempSize", Integer.toString(c+1));
        
        schema.add("");

        schema.add(""
                + "task TW;											// calculate transfer digit and intermediate sum\n"
                + "	input  signed [$u:$l] xi, yi;\n"
                + "	output signed [$u:$l] t, w;\n"
                + "	reg 	 signed [$c:$l] temp;\n"
                + "begin\n"
                + "	temp = xi + yi;\n"
                + "	if (temp >= a) begin\n"
                + "		t = $tempSize'd1;\n"
                + "		temp = (temp - r);\n"
                + "		end\n"
                + "	else if (temp <= -a) begin\n"
                + "		t = -$tempSize'd1;\n"
                + "		temp = temp + r;\n"
                + "		end\n"
                + "	else\n"
                + "		t = $tempSize'd0;\n"
                + "		\n"
                + "	w <= temp[$u:$l];\n"
                + "end\n"
                + "endtask\n"
                + "\n"
                + "task SUM;\n"
                + "	input  signed [$u:$l] t, w;\n"
                + "	output signed [$u:$l] zi;\n"
                + "begin\n"
                + "	zi = t + w;\n"
                + "end\n"
                + "endtask");
        
        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateEndmodule() {
        List<String> schema = new ArrayList<>();
        
        schema.add("");
        schema.add("endmodule");
        schema.add("");
                
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
}
