import time
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

    if count % 4 == 0:
      print "Going to check for new devices!"
      new_appliances = ApplianceModel.Query.filter(new=True).filter(user=my_user)
      for appliance in new_appliances:
        connected = appliance.connect(btSockets,port_max)
        if connected == True:
          print "Added" + appliance.name
          appliance.connected = True
          appliance.new = False
        else:
          appliance.connected = False
        appliance.save()
      # count = 0	# Remove this if we implement the timer code below

    if count % 20 == 0:
    	print "Going to check for timers!"
    	current_time = datetime.now().time()
    	for key in btSockets:
    		try:
    			appliance = ApplianceModel.get(objectId=key)
    		except Exception as e:
    			my_socket = btSockets[key]
    			my_soclet.cose()
    			del btSockets[key]
    			continue
    		# TODO check for empty appliance?
    		if appliance.timerEnabled == True:
				# example time string in parse = '10:33:26'
				FORMAT = '%H:%M:%S' # TODO: const, put this elsewhere

				#delta_t_on = current_time - datetime.strptime(appliance.timerOn, FMT)
				#delta_t_off = current_time - datetime.strptime(appliance.timerOff, FMT)

				delta_t_on = current_time - datetime.strptime("14:40:00", FMT)
				delta_t_off = current_time - datetime.strptime("14:41:00", FMT)

				# TODO better way to do this? will send the toggle a couple times within this 30 sec window
				# also, on and off being close together will mess things up
				should_toggle = false
				if delta_t_on.seconds < 30:
					should_toggle = True
					appliance.power = True
				if delta_t_off.seconds < 30:
					should_toggle = True
					appliance.power = False
				if should_toggle:
					acked = appliance.toggle(btSockets)
        			if acked == True:
          				appliance.synced = True
          				appliance.save()
          			else:  # Failed, so let the regular toggle procedure take care of it
          				appliance.synced = False
          				appliance.save()
		count = 0

    # Loop on dictionary to find unsynced appliances
    for key in btSockets:
      try:
        appliance = ApplianceModel.get(objectId=key)
      except Exception as e:
        # Can return two exceptions but *should* only be one
        my_socket = btSockets[key]
        my_socket.close()
        del btSockets[key]
        continue
      # TODO check for empty appliance
      if appliance.synced == False:
        print appliance.name + " wants to be toggled to " + str(appliance.power)
        acked = appliance.toggle(btSockets)
        if acked == True:
          appliance.synced = True
          appliance.save()
    time.sleep(.25)
    count += 1 

if __name__ == "__main__":
  try:
    main()
  except KeyboardInterrupt:
    print "Keyboard Interrupt...bye..."
  except Exception as e:
    print e.__doc__
    print e.message
