[loadI 1028 => r12; loadI 1024 => r13]
[load r13 => r5; loadI 1032 => r0]
[load r12 => r3; output 1024]
[output 1028; nop]
[output 1024; nop]
[output 1028; nop]
[output 1024; nop]
[store r5 => r0; add r5, r3 => r11]
[store r11 => r0; add r11, r5 => r10]
[store r10 => r0; add r10, r3 => r9]
[store r9 => r0; add r9, r5 => r8]
[store r8 => r0; add r8, r3 => r7]
[store r7 => r0; add r7, r5 => r6]
[output 1028; add r6, r3 => r4]
[store r6 => r0; output 1024]
[store r4 => r0; output 1028]
[add r4, r5 => r2; output 1024]
[store r2 => r0; add r2, r3 => r1]
[store r1 => r0; output 1028]
[nop; nop]
[nop; nop]
[nop; nop]
[nop; nop]
[output 1032; nop]
