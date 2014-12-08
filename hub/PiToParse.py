#!/usr/bin/python
import time

from parse_rest.connection import register
register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk', master_key=None)

from parse_rest.datatypes import Object

class ApplianceModel(Object):
    pass

if __name__ == "__main__":
    # run forever (pulling)
    print "Waiting for a toggle..."
    while True:
        toggledAppliances = ApplianceModel.Query.all().filter(synced=False)
        for appliance in toggledAppliances:
            print "Appliance was toggled to " + str(appliance.power)

            #TODO: updated the arduino's status
            if appliance.power == True:
                print "Sending ON command"
            else:
                print "Sending OFF command"
            # update the database to synced
            appliance.synced=True
            print appliance.user 
            appliance.save()

        #sleep for a second
        time.sleep(1)

