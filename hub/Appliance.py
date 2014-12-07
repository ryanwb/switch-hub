import serial
import bluetooth
from parse_rest.datatypes import Object

onMsg = "\xFF\x01\x01"
offMsg = "\xFF\x01\x00"

def parse_response(response):
  # Will check for 0xFFFE01
  state = 0
  ack = False
  i = 0
  while i < len(response):
    if state == 0 and response[i] == "\xFF":
      state = 1
      i += 1
    elif state == 1 and response[i] == "\xFE":
      state = 2
      i += 1
    elif state == 2 and response[i] == "\x00":
      state = 3
      print "Device acked!"
      ack = True
      break
    else:
      break
  return ack

class ApplianceModel(Object):
  def add(self,sockets):
    print "Adding Appliance"
    # TODO Either serial or socket 
    # Open serial ports and add to dictionary
    #dev_name = "/dev/" + appliance.objectId
    #bluetoothSerial = serial.Serial(dev_name, baudrate=9600)
    #serialPorts[appliance.objectId] = bluetoothSerial

  def toggle(self,sockets):
    print "Toggling Appliance"
    # Send toggle command to bluetooth and parse ACK
    #my_socket = sockets[self.objectId]
    if self.power == True:
      # Toggle device ON
      print "Sending ON"
      #my_socket.send(onMsg)
    else:
      # Toggle device OFF
      print "Sending OFF"
      #my_socket.send(offMsg)
    # Parse response
    #response = my_socket.recv(256)
    response = "\xFF\xFE\x01"
    ack = parse_response(response)
    # If ACKed, sync the appliance
    if ack == True:
      self.synced = True
      #self.save()
    else:
      print "Device didn't ACK!!"
