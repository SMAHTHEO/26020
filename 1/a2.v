// Internal signal and bus declarations
reg [15:0] instruction;       // A register to hold a copy of the instruction if needed
reg [2:0]  opcode;            // A register to hold the opcode extracted from the instruction
reg        branch_taken;      // A signal to indicate whether a branch is taken
wire [15:0] alu_result;       // A wire to hold the ALU result temporarily
wire        zero_flag;        // A wire that is high when ALU result is zero (for condition codes)
wire        carry_flag;       // A wire that indicates a carry out (for condition codes)
wire        sign_flag;        // A wire that indicates the sign of the ALU result (for condition codes)
wire        overflow_flag;    // A wire that indicates if there was an overflow (for condition codes)
