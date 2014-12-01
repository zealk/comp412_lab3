//NAME: COMP412
//NETID: comp412
//SIM INPUT: -i 2000 0 1 2 3 4 5
//OUTPUT: 32
//
// COMP 412, FALL 1997, Lab 3
// Instruction Scheduling
// Report Block 1
// *** Usage before scheduling: ./sim -s 3 -i 2000 0 1 2 3 4 5 < report1.i
loadI 4		=> r1
loadI 2000	=> r2
add r2, r1	=> r3
load r2		=> r10
load r3		=> r11
add r10, r11	=> r20
add r3, r1	=> r4
add r4, r1	=> r5
load r4		=> r12
load r5		=> r13
add r12, r13	=> r21
add r5, r1	=> r6
add r6, r1	=> r7
load r6		=> r14
load r7		=> r15
mult r14, r15	=> r22
mult r13, r12	=> r23
add r22, r23	=> r22
add r21, r20	=> r20
add r22, r20	=> r20
add r7, r1	=> r8
store r20	=> r8
output 2024
// end of block
