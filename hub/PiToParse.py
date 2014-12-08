#!/usr/bin/python
import serial
import time

from parse_rest.connection import register
register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk', master_key=None)

from parse_rest.datatypes import Object

bluetoothSerial = serial.Serial("/dev/rfcomm1", baudrate=9600, timeout=1)
onMsgExample = "\xFF\x01\x01"
offMsgExample = "\xFF\x01\x00"

class ApplianceModel(Object):
    pass

my_user = "zcpXqHWsbD"

if __name__ == "__main__":

    # run forever (pulling)
    print "Waiting for a toggle..."
    while True:
        toggledAppliances = ApplianceModel.Query.all().filter(synced=False).filter(user=my_user)
        for appliance in toggledAppliances:
            print "About to Toggle this device"
            #TODO: updated the arduino's status
            if appliance.power == True:
                print "Sending ON command"
                bluetoothSerial.write(onMsgExample)
                data_in = bluetoothSerial.readline()
                print "Response: " + data_in.encode("hex")
            else:
                print "Sending OFF command"
                bluetoothSerial.write(offMsgExample)
                data_in = bluetoothSerial.readline()
                print "Response: " + data_in.encode("hex")
            # update the database to synced
            appliance.synced=True
            appliance.save()

        #sleep for a second
        time.sleep(1)

