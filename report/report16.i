//NAME: COMP412
//NETID: comp412
//SIM INPUT:
//OUTPUT: 11 12 13
//
// This report block is the contributed block yanxinlu.i
//
// Usage before scheduling: ./sim -3 < report16.i
loadI 1024 => r1
loadI 11 => r11
store r11 => r1
loadI 1028 => r2
loadI 12 => r12
store r12 => r2
loadI 1032 => r3
loadI 13 => r13
store r13 => r3
output 1024
output 1028
output 1032
