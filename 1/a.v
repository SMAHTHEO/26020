
always @(operand_A, operand_B, func, c_in, csh) begin

    result <= 16'b0;
    //flags_out <= 4'b0;

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
// Flags Generation
always @(result, operand_A, operand_B, temp_result, csh, func) begin
    // N and Z flags
    flags_out[3] = result[15]; // N
    flags_out[2] = (result == 16'b0) ? 1'b1 : 1'b0; // Z

    // V flag
    flags_out[1] = ((operand_A[15] & operand_B[15] & ~result[15]) | (~operand_A[15] & ~operand_B[15] & result[15])) ? 1'b1 : 1'b0; // V

    // C flag
    flags_out[0] =(func == 3'b100 || func == 3'b101) ? csh: temp_result[16];

end
