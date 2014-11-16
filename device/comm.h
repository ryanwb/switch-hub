// comm.h
// Constants/interface control between hub and devices

#ifndef COMM_H
#define COMM_H

// Start/sync byte (byte 0)
const char START        = 0xFF;

// Opcodes (byte 1)
const char OP_FLIP	= 0x01;

// OP_FLIP args (byte 2)
const char FLIP_ON	= 0x01;
const char FLIP_OFF	= 0x00;

#endif


