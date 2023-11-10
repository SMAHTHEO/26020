# ** Note: Multiple occurrences of ini variable Show_source are found, only first occurence will be considered.
# QuestaSim-64 vlog 2022.4 Compiler 2022.10 Oct 18 2022
# Start time: 16:26:40 on Nov 10,2023
# vlog -reportprogress 300 -O0 -lint -cover bcsxf /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v 
# -- Compiling module Stump_control_decode
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(68):             srcA = `PC;     // Source A is PC for ALU to add increment
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(68): (vlog-2163) Macro `PC is undefined.
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(68): near ";": syntax error, unexpected ';'.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(69):             alu_func = `ALU_INC; // ALU function is increment
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(69): (vlog-2163) Macro `ALU_INC is undefined.
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(69): near ";": syntax error, unexpected ';'.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(70):             dest = `PC;     // Destination is PC for the result
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(70): (vlog-2163) Macro `PC is undefined.
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(70): near ";": syntax error, unexpected ';'.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(78):                 `OP_ADD: begin
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(78): (vlog-2163) Macro `OP_ADD is undefined.
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(78): near ":": syntax error, unexpected ':'.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(79):                     alu_func = `ALU_ADD;   // Set ALU to perform ADD
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(79): (vlog-2163) Macro `ALU_ADD is undefined.
###### ** while parsing macro expansion: 'MEMORY' starting at /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(89)
# ** at /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(89):         `MEMORY: begin
# ** Error: (vlog-13069) ** while parsing macro expansion: 'MEMORY' starting at /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(89)
# ** at /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(89): near "2": syntax error, unexpected INTEGER NUMBER.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(117):   {N,Z,V,C} = CC;			// Break condition code register into flags
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(117): near "{": syntax error, unexpected '{'.
###### /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(119):      0 : Testbranch =   1;	// Always (true)
# ** Error: (vlog-13069) /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(119): near "=": syntax error, unexpected '='.
# ** Error: /home/c76258yx/Questa/COMP22111/src/Stump/Stump_control_decode.v(119): (vlog-13205) Syntax error found in the scope following 'Testbranch'. Is there a missing '::'?
# End time: 16:26:40 on Nov 10,2023, Elapsed time: 0:00:00
# Errors: 13, Warnings: 0
# /cadtools5/mgc/questasim_2022.4/linux_x86_64/vlog failed.
