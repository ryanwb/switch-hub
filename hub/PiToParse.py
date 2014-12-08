#!/usr/bin/python
import time

from parse_rest.connection import register
register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk', master_key=None)

from parse_rest.datatypes import Object

<<<<<<< HEAD
=======
bluetoothSerial = serial.Serial("/dev/rfcomm1", baudrate=9600, timeout=1)
onMsgExample = "\xFF\x01\x01"
offMsgExample = "\xFF\x01\x00"

>>>>>>> 0ac2f6aeca956084fd766e56521cff66c1113aba
class ApplianceModel(Object):
    pass

my_user = "zcpXqHWsbD"

if __name__ == "__main__":
<<<<<<< HEAD
=======

>>>>>>> 0ac2f6aeca956084fd766e56521cff66c1113aba
    # run forever (pulling)
    print "Waiting for a toggle..."
    while True:
        toggledAppliances = ApplianceModel.Query.all().filter(synced=False).filter(user=my_user)
        for appliance in toggledAppliances:
<<<<<<< HEAD
            print "Appliance was toggled to " + str(appliance.power)

=======
            print "About to Toggle this device"
>>>>>>> 0ac2f6aeca956084fd766e56521cff66c1113aba
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

