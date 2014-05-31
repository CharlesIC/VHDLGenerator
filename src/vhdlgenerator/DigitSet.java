/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

import java.util.List;
import java.util.ArrayList;

public class DigitSet {
    
    // The digit set is of the form
    // D = {-a, ..., a} where a = r - 1
    
    public final List<Integer> digits;
    public final int size;
    public final int a;
    
    public DigitSet (int r) {
        a = r - 1;
        size = 2*r - 1;
        digits = new ArrayList<>(size);
        
        for (int i = -a; i <= a; i++)
            digits.add(i);
    }
    
    public void printDigits () {
        System.out.print("{");
        for (Integer item : digits)
            System.out.println();
    }
    
}
