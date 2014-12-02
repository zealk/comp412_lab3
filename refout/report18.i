[loadI 1000 => r11; loadI 3 => r8]
[load r11 => r6; loadI 2 => r9]
[loadI 1 => r10; loadI 1012 => r1]
[loadI 1008 => r3; loadI 1004 => r5]
[loadI 4 => r7; nop]
[nop; nop]
[store r10 => r5; nop]
[store r9 => r3; nop]
[store r8 => r1; nop]
[store r7 => r6; nop]
[nop; nop]
[nop; nop]
[nop; nop]
[nop; nop]
[load r5 => r4; output 1004]
[load r3 => r2; output 1008]
[load r1 => r0; output 1012]
[nop; nop] // clear out pending ops
[nop; nop] // clear out pending ops
[nop; nop] // clear out pending ops
[nop; nop] // clear out pending ops
