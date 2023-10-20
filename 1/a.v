// Stump ALU
// Implement your Stump ALU here
//
// Created by Paul W Nutter, Feb 2015
//
// ** Update this header **
//

`include "Stump_definitions.v"

// 'include' definitions of function codes etc.
// e.g. can use "`ADD" instead of "'h0" to aid readability
// Substitute your own definitions if you prefer by
// modifying Stump_definitions.v

/*----------------------------------------------------------------------------*/

module Stump_ALU (input  wire [15:0] operand_A,		// First operand
                                 input  wire [15:0] operand_B,		// Second operand
		                          input  wire [ 2:0] func,		// Function specifier
		                          input  wire        c_in,		// Carry input
		                          input  wire        csh,  		// Carry from shifter
		                          output reg  [15:0] result,		// ALU output
		                          output reg  [ 3:0] flags_out);	// Flags {N, Z, V, C}


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
/* Declarations of any internal signals and buses used                        */


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
/* Verilog code                                                               */

reg [16:0] temp_result; // 

always @(operand_A, operand_B, func, c_in, csh) begin
    // 
    result <= 16'b0;
    flags_out <= 4'b0;

    case(func)
        3'b000: begin // ADD
            temp_result = operand_A + operand_B;
            result <= temp_result[15:0];
        end
        3'b001: begin // ADC
            temp_result = operand_A + operand_B + c_in;
            result <= temp_result[15:0];
        end
        3'b010: begin // SUB
            temp_result = operand_A + (~operand_B + 1);
            result <= temp_result[15:0];
        end
        3'b011: begin // SBC
            temp_result = operand_A + (~operand_B + c_in);
            result <= temp_result[15:0];
        end
        3'b100: begin // AND
            result <= operand_A & operand_B;
        end
        3'b101: begin // OR
            result <= operand_A | operand_B;
        end
        3'b110: begin // LD/ST
            // NOP: No operation for ALU
        end
        3'b111: begin // Bcc
            // NOP: No operation for ALU
        end
    endcase
end
// Clock-driven always block to latch the flags
always @(posedge clk) begin
    flags_out <= {result[15], (result == 16'b0), 
                  (operand_A[15] & operand_B[15] & ~result[15]) | (~operand_A[15] & ~operand_B[15] & result[15]), 
                  temp_result[16]};
end


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

/*----------------------------------------------------------------------------*/

endmodule

/*============================================================================*/