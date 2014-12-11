import time
import sys
import os
from datetime import datetime
from parse_rest.connection import register
from Appliance import ApplianceModel

port_max = 1
my_user = "zcpXqHWsbD"

def main():
  print "Register to Parse"
  register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk',master_key=None)

  # Get list of active appliances
  active_appliances = ApplianceModel.Query.filter(user=my_user)
  
  # Connect to appliances
  btSockets = {}
  for appliance in active_appliances:
    connected = appliance.connect(btSockets,port_max)
    if connected == True:
      appliance.connected = True
      acked = appliance.toggle(btSockets)
      if acked == True:
        appliance.synced = True
      else:
        appliance.synced = False
    else:
      appliance.connected = False
      appliance.synced = False
    appliance.save()

  # Go into main loop
  # This loop checks if new devices are added and if devices need to be toggled
  count = 0
  while True:

    if count == 4:
      print "Going to check for new devices!"
      new_appliances = ApplianceModel.Query.filter(new=True).filter(user=my_user)
      for appliance in new_appliances:
        connected = appliance.connect(btSockets,port_max)
        if connected == True:
          print "Added " + appliance.name
          appliance.connected = True
          acked = appliance.toggle(btSockets)
          if acked == True:
            appliance.synced = True
          else:
            appliance.sycned = False
          appliance.new = False
        else:
          appliance.connected = False
        appliance.save()
      count = 0	# Remove this if we implement the timer code below

    # Loop on dictionary to find unsynced appliances
    del_list = []
    for key in btSockets:
      try:
        appliance = ApplianceModel.Query.get(objectId=key)
      except Exception as e:
        # Can return two exceptions but *should* only be one
        print e.message
        my_socket = btSockets[key]
        my_socket.close()
        del_list.append(key)
        continue
      # TODO check for empty appliance
      if appliance.synced == False and appliance.connected == True:
        print appliance.name + " wants to be toggled to " + str(appliance.power)
        acked = appliance.toggle(btSockets)
        if acked == True:
          appliance.synced = True
        appliance.save()
    for key in del_list:
      del btSockets[key]
      print "deleted " + key
    time.sleep(.25)
    count += 1 

if __name__ == "__main__":
  while True:
    try:
      main()
    except KeyboardInterrupt:
      print "Keyboard Interrupt...bye..."
      try:
        sys.exit(0)
      except SystemExit:
        os._exit(0)
    except Exception as e:
      print "Caught exception in main!!!"
      print e.__doc__
      print e.message
