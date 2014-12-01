//NAME: COMP412
//NETID: comp412
//SIM INPUT: -i 2000 0 1 2 3 4 5
//OUTPUT: 37
//
// COMP 412, FALL 1997, Lab 3
// Instruction Scheduling
// report block 2; more complex version of report block 1
// *** Usage before scheduling: ./sim -s 3 -i 2000 0 1 2 3 4 5 <report2.i
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
loadI 2000	=> r40
load r40	=> r50
add r40, r1	=> r41
load r41	=> r51
mult r50, r51	=> r52
add r41, r1	=> r42
load r42	=> r53
add r42, r1	=> r43
load r43	=> r54
add r54, r53	=> r55
add r55, r52	=> r55
add r55, r20	=> r55
store r55	=> r8
output 2024
//end of block
