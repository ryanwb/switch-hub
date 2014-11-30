// comm.h
// Constants/interface control between hub and devices

#ifndef COMM_H
#define COMM_H

const int BUFF_LEN	= 1 << 6;	// 64 bytes

// Start/sync byte (byte 0)
const int START  	= 0xFF;

// Opcodes (byte 1)
const int OP_ACK	= 0xFE;
const int OP_FLIP	= 0x01;

// OP_FLIP args (byte 2)
const int FLIP_ON	= 0x01;
const int FLIP_OFF	= 0x00;

enum State {
	AWAIT_START,		// Await the start/sync byte
	AWAIT_CMD_ID,		// Await the command ID
	AWAIT_FLIP_ID		// Await the flip ID (byte designating on or off)
};

#endif


