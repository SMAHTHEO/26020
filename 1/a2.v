// Stump ALU


module Stump_ALU (input  wire [15:0] operand_A,	
                                 input  wire [15:0] operand_B,	
		                          input  wire [ 2:0] func,	
		                          input  wire        c_in,	
		                          input  wire        csh,  	
		                          output wire  [15:0] result,	
		                          output wire  [ 3:0] flags_out);

wire [16:0] U;
wire [15:0] W, T, D, E,F ;
wire X;

or   I1[15:0] (E, operand_A, W);
and I2[15:0] (D, operand_A, W);
xor I3[15:0] (T, operand_A,W, U[15:0]);
and I4 (flags_out[1], ~func[2], X);
or   I5[15:0] (U[16:1], F, D);
buf I6 (flags_out[3], result[15]);
buf I7 (flags_out[2], ~|result);
xor I8 (X, U[16], U[15]);
and I9[15:0] (F, E, U[15:0]);

assign W = (func[2:1] == 1) ? ~operand_B : operand_B;
assign flags_out[0] = (func[2:1]==0) ? U[16] : (func[2:1]==1) ? ~U[16] : csh;
assign U[0] = (func==1) ? c_in : (func==2) ? 1 : (func==3) ? ~c_in : 0;
assign result = (func==4) ? D : (func==5) ? E : T;



endmodule

/*============================================================================*/
