import time
import serial
#import bluetooth
from parse_rest.connection import register
from Appliance import ApplianceModel


def add_appliance():
  print "Adding Appliance"
  # TODO Check for new appliances
  # TODO If any, add them to dictionary

def toggle_appliance(appliance):
  print "Toggling Appliance"
  # TODO Send toggle command to bluetooth and parse ACK
  # TODO If ACKed, sync the appliance
  appliance.synced = True
  appliance.save()

def main():
  print "Register to Parse"
  register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk',master_key=None)

  # Get list of active appliances
  #active_appliances = ApplianceModel.Query.all().filter(active=True)
  
  # Connect to appliances
  #serialPorts = {}
  #btSockets = {}
  #for appliance in active_appliances:
    # Open serial ports and add to dictionary
    #dev_name = "/dev/" + appliance.objectId
    #bluetoothSerial = serial.Serial(dev_name, baudrate=9600)
    #serialPorts[appliance.objectId] = bluetoothSerial

  # Go into main loop
  # This loop checks if new devices are added and if devices need to be toggled
  count = 0
  while True:
    if count == 5:
      print "Going to check for new devices!"
      add_appliance()
      count = 0
    toggledAppliances = ApplianceModel.Query.all().filter(synced=False)
    for appliance in toggledAppliances:
      print appliance.name + " wants to be toggled to " + str(appliance.power)
      toggle_appliance(appliance)
    time.sleep(1)
    count += 1 

if __name__ == "__main__":
  main()
