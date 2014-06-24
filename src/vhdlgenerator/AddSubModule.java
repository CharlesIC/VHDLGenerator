/*
 * Final Year Project of Karol Gancarz
 * BEng EIE at Imperial College London
 * May 2014
 */

package vhdlgenerator;

public class AddSubModule {
    
    private final int r;
    private final int a;
    private final int n;
    private final Operation op;
    
    private final int delay = 2;
    private final int cycles;
    
    public AddSubModule(int r, int n, int a, Operation op) {
        this.r = r;
        this.n = n;
        this.a = a;
        this.op = op;
        
        this.cycles = n + delay;
    }
    
    public int[] result(int[] x, int[] y) {
        int[] z = new int[x.length+1];
        
        if (x.length != y.length)
            System.exit(-1);
        
        int t = 0;                          // transfer digit
        int w = 0;                          // intermediate sum
        int w_latch = 0;
        
        for (int i = 0; i < cycles; i++) {
            // Step 1: Calculate T and W
            w = w_latch;
            
            if (i < n)
                w_latch = x[i] + y[i];
            else
                w_latch = 0;
            
            if (w_latch >= a) {
                w_latch -= r;
                t = 1;
            }
            else if (w_latch <= -a) {
                w_latch += r;
                t = -1;
            }
            else
                t = 0;
            
            // Step 2: Calculate zi
            if (i == 0)
                z[i] = 0;
            else
                z[i] = t + w;
        }
        
        return z;
    }
}
