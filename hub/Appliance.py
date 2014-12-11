import bluetooth
from parse_rest.datatypes import Object

onMsg = "\xFF\x01\x01"
offMsg = "\xFF\x01\x00"

def parse_response(response):
  # Will check for 0xFFFE01
  ack = False
  if response == "\xFE":
    ack = True
  return ack

class ApplianceModel(Object):
  def connect(self,sockets,port):
    print "Adding Appliance"
    # Establish socket and add to dictionary
    my_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    try:
      my_socket.connect((self.bluetooth,port))
    except Exception as e:
      print "Caught Exception while trying to connect!"
      return False
    sockets[self.objectId] = my_socket
    return True

  def toggle(self,sockets):
    print "Toggling Appliance"
    # Send toggle command to bluetooth and parse ACK
    my_socket = sockets[self.objectId]
    if self.power == True:
      # Toggle device ON
      print "Sending ON"
      try:
        my_socket.send(onMsg)
      except Exception as e:
        print "Caught Exception while sending!"
        self.connected = False
        return False
    else:
      # Toggle device OFF
      print "Sending OFF"
      try:
        my_socket.send(offMsg)
      except Exception as e:
        print "Caught Exception while sending!"
        self.connected = False
        return False
    # Parse response
    try:
      response = my_socket.recv(256)
    except Exception as e:
      print "Caught Exception while recieving!"
      self.connected = False
      return False
    ack = parse_response(response)
    return ack

  def delete(self,sockets):
    print "Gonna delete appliance"
    # If sockets work, we can delete appliances!!
    #my_socket = sockets[self.objectId)
    #my_socket.close()
    #del sockets[self.objectId]
    # how to delete in Parse?
