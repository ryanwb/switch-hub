import serial
import time

bluetoothSerial = serial.Serial("/dev/rfcomm1", baudrate=9600)

onMsgExample = bytes([0xFF, 0x01, 0x01])
offMsgExample = bytes([0xFF, 0x01, 0x00])

while True:
  print "Sending ON command"
  bluetoothSerial.write(onMsgExample)
  time.sleep(5)
  print "Sending OFF command"
  bluetoothSerial.write(offMsgExample)
  time.sleep(5)
