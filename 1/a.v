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
    result = 16'b0;
    flags_out = 4'b0;

    case(func)
        3'b000: begin // ADD
            temp_result = operand_A + operand_B;
            result = temp_result[15:0];
        end
        // ... [其他的case项]
    endcase
end

// Flags Generation
always @(result, operand_A, operand_B, temp_result, csh, func) begin
    // N and Z flags
    flags_out[3] = result[15]; // N
    flags_out[2] = (result == 16'b0) ? 1'b1 : 1'b0; // Z

    // V flag for SUB
    if (func == 3'b010) begin
        if ((~operand_B[15] & operand_A[15] & ~result[15]) | 
            (operand_B[15] & ~operand_A[15] & result[15])) begin
            flags_out[1] = 1'b1; // V
        end else begin
            flags_out[1] = 1'b0;
        end
    end else begin
        // ... [其他的逻辑]
    end
end


always @(operand_A, operand_B, func, c_in, csh) begin
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
            temp_result = operand_A + (~operand_B + 1'b1);
            result <= temp_result[15:0];
        end
        3'b011: begin // SBC
            if (c_in == 1'b1) {
                temp_result = operand_A + (~operand_B);
            } else {
                temp_result = operand_A + (~operand_B + 1'b1);
            }
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

// Flags Generation
always @(result, operand_A, operand_B, temp_result, csh, func) begin
    // N and Z flags
    flags_out[3] = result[15]; // N
    flags_out[2] = (result == 16'b0) ? 1'b1 : 1'b0; // Z

    // V flag for SUB
    if (func == 3'b010) {
        if ((~operand_B[15] & operand_A[15] & ~result[15]) | 
            (operand_B[15] & ~operand_A[15] & result[15])) {
            flags_out[1] = 1'b1; // V
        } else {
            flags_out[1] = 1'b0;
        }
    } else {
        if ((operand_A[15] & operand_B[15] & ~result[15]) | 
            (~operand_A[15] & ~operand_B[15] & result[15])) {
            flags_out[1] = 1'b1; // V
        } else {
            flags_out[1] = 1'b0;
        }
    }

    // C flag
    if (func == 3'b100 || func == 3'b101) { // AND or OR
        flags_out[0] = csh;
    } else {
        flags_out[0] = temp_result[16]; // C
    }
end




/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

/*----------------------------------------------------------------------------*/

endmodule
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_ALU.v(57): near "=": syntax error, unexpected '=', expecting ++ or --.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_ALU.v(86):         if ((~operand_B[15] & operand_A[15] & ~result[15]) | 
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_ALU.v(86): near "if": syntax error, unexpected if.
# End time: 14:53:57 on Oct 20,2023, Elapsed time: 0:00:00
# Errors: 2, Warnings: 0
# /cadtools5/mgc/questasim_2022.4/linux_x86_64/vlog failed.
/*============================================================================*/
