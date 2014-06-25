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

public class Simulator implements Module {
    
    private final int r;
    private final int n;
    private final int c;
    private final int numTests;                 // number of tests
    private final DigitSet D;
    private final Operation op;
    
    private final String name;
    private final String filename;
    
    private final int delay = 2;
    private final int cycles;
    
    // Values of different variables for the generator
    private final Map<String, String> fields = new HashMap<>();
    
    // Arithmetic & test modules
    Adder addSub;
    Tester test;
    
    public Simulator(int r, int n, int numTests, Operation op) {
        this.r = r;
        this.n = n;
        this.D = new DigitSet(r);
        this.c = D.c;
        this.op = op;
        this.numTests = numTests;
        cycles = n + delay;
        
        addSub = new Adder(r, D, op);
        test = new Tester(r, n, D, numTests, op);
        
        // Set component name
        name = op.shortName + "_sim_r" + r + "_n" + n;
        filename = name + ".v";
        
        fields.put("r", Integer.toString(r));
        fields.put("n", Integer.toString(n));
        fields.put("c", Integer.toString(c));
        fields.put("cycles", Integer.toString(cycles));
        fields.put("numTests", Integer.toString(numTests));
        fields.put("name", name);
        
        // Escape the $ character in system tasks
        fields.put("display", "$display");
        fields.put("write", "$write");
        fields.put("stop", "$stop");
    }
    
    @Override
    public void generate() {
        generateModuleDeclaration();
        generateRegsParams();
        initialiseModules();
        generateTestProcess();
        generateTasks();
        generateEndmodule();
    }
    
    @Override
    public String initialise() {return "";}
    
    private void generateModuleDeclaration() {
        List<String> schema = new ArrayList<>();

        schema.add(""
                + "// Simulation time unit = 1 ns\n"
                + "// Simulation timestep  = 10 ps\n"
                + "`timescale 1ns/10ps\n"
                + "\n"
                + "module $name (\n"
                + "	input CLOCK_50\n"
                + ");"
        );
        
        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    private void generateRegsParams() {
        List<String> schema = new ArrayList<>();
        
        fields.put("digitU", Integer.toString(c-1));
        fields.put("resultU", Integer.toString(cycles*c-1));
        fields.put("opU", Integer.toString(n*c-1));
        fields.put("expU", Integer.toString((n+1)*c-1));
        fields.put("l", "0");
        fields.put("testSelU", Integer.toString((int) Math.ceil(Math.log(numTests)/Math.log(2)) - 1));
        fields.put("sizeZ", Integer.toString(n+1));
        
        // Adder ports 
        fields.put("addName", addSub.name);
        fields.put("addClk", "adder_clock");
        fields.put("addReset", "adder_reset");
        fields.put("addEn", "adder_enable");
        fields.put("xi", "xi");
        fields.put("yi", "yi");
        fields.put("zi", "zi");
        
        // Tester ports
        fields.put("testerName", test.name);
        fields.put("testNo", "testNo");
        fields.put("x", "x");
        fields.put("y", "y");
        fields.put("expected", "expected");

        schema.add(""
                + "//===================================\n"
                + "//		Test parameters\n"
                + "//==================================="
                + "");
        
        schema.add("parameter cycles = $cycles;");
        schema.add("parameter numTests = $numTests;                     // number of tests to be performed");
        
        schema.add("\n\n");
        
        schema.add(""
                + "//===================================\n"
                + "//		REG/WIRE declarations\n"
                + "//==================================="
                + "");
        
        schema.add("reg $addReset;");
        schema.add("reg $addEn;");
        schema.add("reg $addClk;");

        schema.add(""
                + "\n// Test variables //\n"
                + "wire signed [$digitU:$l] $zi;                          // Single digit of the result\n"
                + "reg  signed [$digitU:$l] $xi, $yi;                      // Single digits of the operands\n"
                + "reg  signed [$resultU:$l] result;                     // The result of addition or subtraction\n"
                + "\n"
                + "wire [$opU:$l] $x, $y;                          // The two operands to be added or subtracted\n"
                + "wire [$expU:$l] $expected;                     // The correct result used to test the arithmetic module\n"
                + "\n"
                + "wire correct = (result[$expU:$l] == expected);\n"
                + "\n"
                + "reg [$testSelU:$l] $testNo;                            // Current test number\n"
                + "reg [$testSelU:$l] i;                                 // Current cycle in a test\n"
                + "");
        
        schema.add("");

        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    public void initialiseModules() {
        List<String> schema = new ArrayList<>();
        
        schema.add("");
        schema.add(addSub.initialise());
        schema.add(test.initialise());
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    public void generateTestProcess() {
        List<String> schema = new ArrayList<>();
        
        schema.add("");
        
        schema.add(""
                + "//========================================\n"
                + "// 			Test process\n"
                + "//========================================\n"
                + "initial begin\n"
                + "	i = 0;\n"
                + "	$xi = 0;\n"
                + "	$yi = 0;\n"
                + "	$testNo = 0;\n"
                + "	$addReset = 0;\n"
                + "	$addClk = 0;\n"
                + "	$addEn = 0;\n"
                + "	\n"
                + "	// The outer loop switches between tests\n"
                + "	// The inner loop controls the flow of the current test\n"
                + "	\n"
                + "	repeat ($numTests) begin\n"
                + "		// Reset the adder\n"
                + "		$addReset = 1;\n"
                + "		#200\n"
                + "		$addReset = 0;\n"
                + "		$addEn = 1;\n"
                + "		\n"
                + "		//$display(\"x = %d, y = %d\", $x, $y);\n"
                + "		$display(\"Test  #%d\", $testNo);\n"
                + "		$write(\"x =\"); displayNumber($x); $write(\", \");\n"
                + "		$write(\"y =\"); displayNumber($y);\n"
                + "		$display();\n"
                + "		\n"
                + "		i = 0;\n"
                + "		repeat ($cycles) begin\n"
                + "			$addClk = 1;\n"
                + "			#100 \n"
                + "			\n"
                + "			if (i < $n) begin\n"
                + "				$xi = $x[$c*($n-i)-1-:$c];					// Load the next x digit\n"
                + "				$yi = $y[$c*($n-i)-1-:$c];					// Load the next y digit\n"
                + "				end\n"
                + "			else begin\n"
                + "				$xi = 0;\n"
                + "				$yi = 0;\n"
                + "				end\n"
                + "			\n"
                + "			result[$c*($cycles-i)-1-:$c] = $zi;			// Read the previous digit of the result\n"
                + "			\n"
                + "			i = i + 1'b1;\n"
                + "			$addClk = 0;\n"
                + "			#100\n"
                + "			\n"
                + "			$display(\"xi = %d, yi = %d, zi = %d\", $xi, $yi, $zi);\n"
                + "		end\n"
                + "		\n"
                + "		$write(\"z =\"); displayNumber(result[$expU:0]);\n"
                + "		$display();\n"
                + "		if (correct) $display(\"Correct\\n\");\n"
                + "		else			 $display(\"Error\\n\");\n"
                + "		\n"
                + "		$addEn = 0;\n"
                + "		$testNo = $testNo + 1'b1;\n"
                + "	end\n"
                + "	\n"
                + "	// End simulation\n"
                + "	$stop;\n"
                + "end"
                + "");
        
        schema.add("");
        
        FormLetterGenerator.generateCode(schema, fields, filename);
    }
    
    public void generateTasks() {
        List<String> schema = new ArrayList<>();
        schema.add("");
        
        fields.put("loopU", Integer.toString((int) Math.ceil(Math.log(n+1)/Math.log(2)) - 1));
        
        schema.add(""
                + "task displayNumber;\n"
                + "	input [$expU:0] num;		// number to display\n"
                + "	reg signed [$digitU:0] digit;\n"
                + "	reg [$loopU:0] i;\n"
                + "begin\n"
                + "	i = 0;\n"
                + "	repeat ($sizeZ) begin\n"
                + "		digit = num[$c*($sizeZ-i)-1-:$c];\n"
                + "		$write(\" %d\", digit);\n"
                + "		i = i + 1'b1;\n"
                + "	end\n"
                + "end\n"
                + "endtask"
                + "");
        
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
