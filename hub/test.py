import serial
import time

bluetoothSerial = serial.Serial("/dev/rfcomm2", baudrate=9600, timeout=1)
print bluetoothSerial.name

onMsgExample = "\xFF\x01\x01"
offMsgExample = "\xFF\x01\x00"

while True:
  print "Sending ON command"
  bluetoothSerial.write(onMsgExample)
  data_in =  bluetoothSerial.readline();
  print data_in.encode("hex")
  time.sleep(1)
  print "Sending OFF command"
  bluetoothSerial.write(offMsgExample)
  data_in =  bluetoothSerial.readline();
  print data_in.encode("hex")
  time.sleep(1)
