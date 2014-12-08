// node.ino
// Code running on Arduino acting as the node in the SwitchHub configuration

#include "comm.h"

const int device_pin = 9;	// Pin out to switch the AC relay
byte wbuffer[BUFF_LEN];		// Write/output buffer
State s;					// State of the state machine determining how the Bluetooth byte stream is parsed

// Power on device
void flipOn() {
	digitalWrite(device_pin, LOW);
}

// Power off device
void flipOff() {
	digitalWrite(device_pin, HIGH);
}

void sendAck(byte op) {
	// wbuffer[0] = START;
	// wbuffer[1] = OP_ACK;
	// wbuffer[2] = op;
	// Serial.write(wbuffer, 3);
	// Send a single-byte ack
	wbuffer[0] = OP_ACK;
	Serial.write(wbuffer,1);
}

void setup() {
	// Start serial communication to bluetooth module
	// Supported Baud rates: 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400
	Serial.begin(9600);
	Serial.flush();

	pinMode(device_pin, OUTPUT);

	// Testing init sequence
	for (int i = 1; i <= 3; i++) {
		flipOn();
		delay(500*i);
		flipOff();
		delay(500*i);
	}

	// Set state to AWAIT_START to wait for the sync byte
	s = AWAIT_START;
}

void loop()
{
	// Listen for data on the serial connection
    if (Serial.available() > 0)
    {
    	/* State machine logic:
    	 * 1. AWAIT_START
    	 * 2. AWAIT_CMD_ID
    	 * 3. Await optional additional params for command
    	 */
    	 
        int x = Serial.read();	// Read one byte

        if (x == -1)			// Read error (nothing in input buffer)
        {
        	s = AWAIT_START;
        }
        else if (x == START)	// Sync byte is reserved independent of state (may change later)
        {
        	s = AWAIT_CMD_ID;
        }
        else
        {
	        switch (s)
	        {
	        	case AWAIT_CMD_ID:			// Await command ID
	        		if (x == OP_FLIP)
	        			s = AWAIT_FLIP_ID;
	        		else
	        			s = AWAIT_START;
	        		break;

        		case AWAIT_FLIP_ID:			// Await flip ID
        			if (x == FLIP_ON) {
        				flipOn();
        				sendAck((byte)OP_FLIP);
        			}
        			else if (x == FLIP_OFF) {
        				flipOff();
        				sendAck((byte)OP_FLIP);
        			}
        			s = AWAIT_START;
        			break;

        		default:					// Await start/sync byte
        			// Did not receive start byte; do nothing
        			break;
	        }
	    }
    }
    delay(50);
}

