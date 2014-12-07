import time
from parse_rest.connection import register
from Appliance import ApplianceModel

def main():
  print "Register to Parse"
  register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk',master_key=None)

  # Get list of active appliances
  #active_appliances = ApplianceModel.Query.all().filter(active=True)
  
  # Connect to appliances
  #serialPorts = {}
  btSockets = {}
  #for appliance in active_appliances:
    #add_appliance(appliance,btSockets) 

  # Go into main loop
  # This loop checks if new devices are added and if devices need to be toggled
  count = 0
  while True:
    if count == 5:
      print "Going to check for new devices!"
      #new_appliances = ApplianceModel.Query.all().filter(active=True).filter(new=True)
      #for appliance in new_appliances:
        #appliance.add(btSockets)
      count = 0
    toggledAppliances = ApplianceModel.Query.all().filter(synced=False)
    for appliance in toggledAppliances:
      print appliance.name + " wants to be toggled to " + str(appliance.power)
      appliance.toggle(btSockets)
    time.sleep(1)
    count += 1 

if __name__ == "__main__":
  main()
