module sub_tester_r4_n6 (
   input       [3:0] testSelect,
   output reg	[17:0] x,				// First number to add
   output reg	[17:0] y,				// Second number to add
   output reg	[20:0] z				// Correct result
);


always @(testSelect)
    case (testSelect)
        4'd0:
            begin
                x =       {3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0};
                y =       {3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0};
                z = {3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0};
            end

        4'd1:
            begin
                x =       {-3'd3, -3'd3, -3'd3, -3'd3, -3'd3, -3'd3};
                y =       {-3'd3, -3'd3, -3'd3, -3'd3, -3'd3, -3'd3};
                z = {3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0};
            end

        4'd2:
            begin
                x =       {3'd3, 3'd3, 3'd3, 3'd3, 3'd3, 3'd3};
                y =       {3'd3, 3'd3, 3'd3, 3'd3, 3'd3, 3'd3};
                z = {3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0, 3'd0};
            end

        4'd3:
            begin
                x =       {3'd1, 3'd0, 3'd0, 3'd1, 3'd2, 3'd1};
                y =       {3'd2, -3'd2, -3'd3, -3'd1, -3'd2, -3'd2};
                z = {3'd0, -3'd1, 3'd3, -3'd1, 3'd3, 3'd1, -3'd1};
            end

        4'd4:
            begin
                x =       {-3'd3, -3'd3, 3'd2, -3'd1, -3'd1, 3'd2};
                y =       {-3'd1, 3'd0, -3'd2, 3'd0, -3'd3, 3'd1};
                z = {3'd0, -3'd3, 3'd2, 3'd0, -3'd1, 3'd2, 3'd1};
            end

        4'd5:
            begin
                x =       {-3'd1, -3'd2, 3'd0, 3'd0, 3'd2, 3'd2};
                y =       {-3'd2, 3'd0, -3'd2, -3'd1, 3'd1, -3'd3};
                z = {3'd0, 3'd1, -3'd2, 3'd2, 3'd1, 3'd2, 3'd1};
            end

        4'd6:
            begin
                x =       {3'd0, -3'd2, 3'd2, 3'd0, 3'd0, 3'd2};
                y =       {3'd0, 3'd1, -3'd3, 3'd0, -3'd3, 3'd2};
                z = {3'd0, -3'd1, 3'd2, 3'd1, 3'd1, -3'd1, 3'd0};
            end

        4'd7:
            begin
                x =       {-3'd1, 3'd0, -3'd2, 3'd1, -3'd1, -3'd1};
                y =       {-3'd3, 3'd1, -3'd1, 3'd1, -3'd3, 3'd2};
                z = {3'd0, 3'd2, -3'd1, -3'd1, 3'd0, 3'd1, 3'd1};
            end

        4'd8:
            begin
                x =       {3'd1, 3'd1, 3'd0, -3'd2, -3'd2, 3'd0};
                y =       {-3'd1, -3'd1, -3'd3, -3'd1, -3'd2, -3'd2};
                z = {3'd0, 3'd2, 3'd3, -3'd1, -3'd1, 3'd0, 3'd2};
            end

        4'd9:
            begin
                x =       {-3'd2, -3'd1, 3'd1, 3'd2, 3'd2, 3'd2};
                y =       {-3'd2, 3'd1, -3'd2, -3'd1, 3'd0, 3'd0};
                z = {3'd0, 3'd0, -3'd1, 3'd0, -3'd1, 3'd2, 3'd2};
            end

        default:
            begin
                x = 0;
                y = 0;
                z = 0;
            end
    endcase


endmodule

