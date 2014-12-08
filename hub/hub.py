import time
from parse_rest.connection import register
from Appliance import ApplianceModel

port_max = 1
my_user = "zcpXqHWsbD"

def main():
  print "Register to Parse"
  register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk',master_key=None)

  # Get list of active appliances
  active_appliances = ApplianceModel.Query.all().filter(user=my_user)
  
  # Connect to appliances
  btSockets = {}
  for appliance in active_appliances:
    connected = appliance.connect(btSockets,port_max)
    if connected == True:
      appliance.connected = 1
      acked = appliance.toggle(btSockets)
      if acked == True:
        appliance.synced = True
      else:
        appliance.synced = False
    else:
      appliance.connected = 0
      appliance.synced = False
    appliance.save()

  # Go into main loop
  # This loop checks if new devices are added and if devices need to be toggled
  count = 0
  while True:
    if count == 4:
      print "Going to check for new devices!"
      #new_appliances = ApplianceModel.Query.all().filter(new=True)
      #for appliance in new_appliances:
        #connected = appliance.connect(btSockets,port_max)
	#if connected == True:
	  #appliance.connected = 1
	  #appliance.new = False
	#else:
	  #appliance.connected = 0
	#appliance.save()
      count = 0
    toggledAppliances = ApplianceModel.Query.all().filter(synced=False).filter(user=my_user)
    for appliance in toggledAppliances:
      print appliance.name + " wants to be toggled to " + str(appliance.power)
      acked = appliance.toggle(btSockets)
      if acked == True:
        appliance.synced = True
        appliance.saved()
    time.sleep(.25)
    count += 1 

if __name__ == "__main__":
  main()
