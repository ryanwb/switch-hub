import time
from parse_rest.connection import register
from Appliance import ApplianceModel

def add_appliance():
  print "Adding Appliance"

def toggle_appliance():
  print "Toggling Appliance" 

def main():
  print "Register to Parse"
  #register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk',master_key=None)

  # Get list of active appliances
  #active_appliances = ApplianceModel.Query.all().filter(active=True)
  
  # Connect to appliances
  serialDevices = {}
  for appliance in active_appliances:
    #TODO open serial ports and add to dictionary 

if __name__ == "__main__":
  main()
