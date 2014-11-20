#!/usr/bin/python
import time

from parse_rest.connection import register
register('kPTnWzHeMI900B83Vee52eXYqQWLrJGUBcc4XuJu', 'FWvTqnNw4IKDIHb70oyzqK0CjH3y3TNW8wvx8ytk', master_key=None)

from parse_rest.datatypes import Object

class ApplianceModel(Object):
    pass

if __name__ == "__main__":
    # delete the current rows
    print "Clearing table..."
    all_appliances = ApplianceModel.Query.all()
    for appliance in all_appliances:
        appliance.delete()

    # populate connected appliances
    print "Creating appliances..."
    #TODO: get the list of arduinos connected to the hub
    appliance1 = ApplianceModel(applianceId=1, power=False, synced=True)
    appliance2 = ApplianceModel(applianceId=2, power=True, synced=True)
    appliance3 = ApplianceModel(applianceId=3, power=False, synced=True)
    appliance4 = ApplianceModel(applianceId=4, power=True, synced=True)
    appliance1.save()
    appliance2.save()
    appliance3.save()
    appliance4.save()

    # run forever (pulling)
    print "Waiting for a toggle..."
    while True:
        toggledAppliances = ApplianceModel.Query.all().filter(synced=False)
        for appliance in toggledAppliances:
            print "Appliance " + str(appliance.applianceId) + " was toggled to " + str(appliance.power)

            #TODO: updated the arduino's status

            # update the database to synced
            appliance.synced=True
            appliance.save()
            print "Appliance " + str(appliance.applianceId) + " was synced by the hub "

        #sleep for a second
        time.sleep(1)

