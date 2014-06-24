module online_sub_r4 (
   input clk, reset, en,				// asynchronous reset to clear all latches; control signals: enable and clock
   input       signed [2:0] xi,		// (i+2)th digit of the x input
   input       signed [2:0] yi,		// (i+2)th digit of the y input
   output reg  signed [2:0] zi		// ith digit of the result
);

parameter r = 4;
parameter a = 3;

reg signed [2:0] t;							// transfer digit
reg signed [2:0] w;							// intermediate sum


always @(posedge clk, posedge reset)
	begin
		if (reset)
			begin
				// Clear all latches
				t = 0;
				w = 0;
				zi = 0;
			end
		else if (en)
			begin
				TW(xi, -yi, t, w);
				SUM(t, w, zi);
			end
	end


task TW;											// calculate transfer digit and intermediate sum
	input  signed [2:0] xi, yi;
	output signed [2:0] t, w;
	reg 	 signed [3:0] temp;
begin
	temp = xi + yi;
	if (temp >= a) begin
		t = 4'd1;
		temp = (temp - r);
		end
	else if (temp <= -a) begin
		t = -4'd1;
		temp = temp + r;
		end
	else
		t = 4'd0;
		
	w <= temp[2:0];
end
endtask

task SUM;
	input  signed [2:0] t, w;
	output signed [2:0] zi;
begin
	zi = t + w;
end
endtask


endmodule

