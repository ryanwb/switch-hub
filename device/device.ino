// node.ino
// Code running on Arduino acting as the node in the SwitchHub configuration

#include "comm.h"

// char buff[64];

const int device_pin = 7;	// Pin out to switch the AC relay

void setup() {
	// Start serial communication to bluetooth module
	// Supported Baud rates: 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400
	Serial.begin(9600);
	pinMode(device_pin, OUTPUT);

	// Testing
	delay(1000);
	digitalWrite(device_pin, HIGH);
	delay(1000);
	digitalWrite(device_pin, LOW);
	delay(1000);
	digitalWrite(device_pin, HIGH);
	delay(200);
	digitalWrite(device_pin, LOW);
	delay(1000);
	digitalWrite(device_pin, HIGH);
	delay(200);
	digitalWrite(device_pin, LOW);
	delay(1000);
	digitalWrite(device_pin, HIGH);
	delay(200);
}

void flipOn()
{
	digitalWrite(device_pin, LOW);
}

void flipOff()
{
	digitalWrite(device_pin, HIGH);
}

void handleFlip() {
	switch(Serial.read())
	{
		case FLIP_ON:
			flipOn();
			break;
		case FLIP_OFF:
			flipOff();
			break;
		default:
			break;
	}
}

void quickTest()
{
	digitalWrite(device_pin, LOW);
	delay(100);
	digitalWrite(device_pin, HIGH);
	delay(100);
	digitalWrite(device_pin, LOW);
	delay(100);
	digitalWrite(device_pin, HIGH);
	delay(100);
}

void loop()
{
	if (Serial.available() > 0)
	{
		quickTest();
		if (Serial.read() == START)
		{
			switch(Serial.read())
			{
				case OP_FLIP:
					handleFlip();
					break;

				default:
					break;
			}
		}
    }
}



