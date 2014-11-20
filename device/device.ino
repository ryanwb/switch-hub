// node.ino
// Code running on Arduino acting as the node in the SwitchHub configuration

#include "comm.h"

// char buff[64];

const int device_pin = 7;	// Pin out to switch the AC relay

void flipOn() {
	digitalWrite(device_pin, LOW);
}

void flipOff() {
	digitalWrite(device_pin, HIGH);
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
}

void loop()
{
	// Listen for data on the serial connection
    if (Serial.available() > 0)
    {
        char x = Serial.read();
        if (x == 0xFF) {
        	Serial.println("on");
        	flipOn();
        }
        else if (x == 0xFE) {
        	Serial.println("off");
        	flipOff();
        }
        else {
        	Serial.println(x);
        }
    }
    delay(50);
}

