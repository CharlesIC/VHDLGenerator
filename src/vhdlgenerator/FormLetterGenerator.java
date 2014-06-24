/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ListIterator;

public class FormLetterGenerator {
    
    public static void generateCode(List<String> schema, Map<String, String> fields, String filename) {

        String line;
        boolean comment = true;
        ListIterator<String> it = schema.listIterator();
        
        // Iterate over all lines
        while (it.hasNext()) {
            line = it.next();
            String codeLine = "";

            //comment = line.startsWith("\t") || line.startsWith("//");
            
            // If this is a comment, print it on the same line
            if (!comment) {
                codeLine += '\n';
            }
            
            if (it.hasNext())
                comment = schema.get(it.nextIndex()).startsWith("\t") ||
                          schema.get(it.nextIndex()).startsWith("//");
            else
                comment = false;
            
            // Examine every character in the line
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                
                if (c == '$') {
                    int stopIndex = i + 1;
                    while (Character.isLetterOrDigit(line.charAt(stopIndex))) {
                        stopIndex++;
                        if (stopIndex == line.length())
                            break;
                    }
                    
                    String key = line.substring(i+1, stopIndex);
                    i = stopIndex - 1;
                    
//                    int nextSpace = line.indexOf(' ', i+1);
//                    String key = line.substring(i+1, nextSpace);
//                    i = nextSpace - 1;
                    
                    codeLine += fields.get(key);
                }
                else
                    codeLine += Character.toString(c);
            }
            
            // Print line to file
            writeVrilog(codeLine, filename);
        }
        
        writeVrilog("\n", filename);
    }

    public static void writeVrilog(String schema, String filename) {
        try {
            FileWriter file = new FileWriter(filename, true);
            try (BufferedWriter writer = new BufferedWriter(file)) {
                writer.write(schema);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
